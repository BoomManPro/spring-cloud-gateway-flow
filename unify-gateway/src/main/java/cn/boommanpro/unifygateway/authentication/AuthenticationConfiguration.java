package cn.boommanpro.unifygateway.authentication;

import java.io.IOException;
import java.util.Collection;
import javax.annotation.Resource;
import javax.servlet.*;

import cn.boommanpro.unifygateway.cas.CasUserDetailsService;
import cn.boommanpro.unifygateway.keystore.AutoImportRunner;
import cn.boommanpro.unifygateway.keystore.CertificateProperties;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

/**
 * @author wangqimeng
 * @date 2019/12/4 18:53
 */
@Slf4j
@Configuration

public class AuthenticationConfiguration {

    @Bean
    @ConfigurationProperties("authentication")
    public AuthenticationConfigProperties authenticationConfigProperties() {
        return new AuthenticationConfigProperties();
    }

    @Order(1)
    @Configuration
    @EnableWebSecurity(debug = true)
    @ConditionalOnProperty(prefix = "authentication", name = "type", havingValue = "normal_authentication", matchIfMissing = true)
    public static class NormalAuthenticationConfiguration extends WebSecurityConfigurerAdapter {

        @Resource
        private UserDetailsService adminDetailServiceImpl;

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public NormalAuthenticationServiceImpl normalAuthenticationService(PasswordEncoder passwordEncoder) {
            return new NormalAuthenticationServiceImpl(passwordEncoder);
        }

        @Bean
        public AuthenticationProvider authProvider() {
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(adminDetailServiceImpl);
            authProvider.setPasswordEncoder(passwordEncoder());
            return authProvider;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/api/**")
                    .authorizeRequests()
                    .antMatchers("/api/**")
                    .authenticated()
                    .and()
                    .userDetailsService(adminDetailServiceImpl)
                    .formLogin()
                    .loginProcessingUrl("/api/login")
                    .successHandler(new SuccessHandler())
                    .failureHandler(new FailureHandler())
                    .permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/api/logout")
                    .logoutSuccessHandler(new LogoutHandler())
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .and()
                    .exceptionHandling().authenticationEntryPoint(new MacLoginUrlAuthenticationEntryPoint("/api/login"))
                    .and()
                    .csrf().disable();

        }
    }

    @Order(2)
    @Configuration
    @EnableWebSecurity(debug = true)
    @ConditionalOnProperty(prefix = "authentication", name = "type", havingValue = "cas_authentication")
    public static class CasAuthenticationConfiguration extends WebSecurityConfigurerAdapter {

        @Resource
        private CasConfigProperties casProperties;

        @Bean
        @ConfigurationProperties(prefix = "sso")
        public CertificateProperties certificateProperties() {
            return new CertificateProperties();
        }

        @Bean
        @ConfigurationProperties(prefix = "authentication.cas-config")
        public CasConfigProperties casProperties() {
            return new CasConfigProperties();
        }

        @Bean
        @ConditionalOnProperty(prefix = "sso", value = "auto-import-certificate", havingValue = "true", matchIfMissing = false)
        public AutoImportRunner autoImportRunner() {
            return new AutoImportRunner();
        }

        /**
         * 定义认证用户信息获取来源，密码校验规则等
         */
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            super.configure(auth);
            auth.authenticationProvider(casAuthenticationProvider());
        }

        /**
         * 定义安全策略
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().and()
                    .headers()
                    .frameOptions().sameOrigin()
                    .xssProtection()
                    .block(true);

            http
                    .headers()
                    .cacheControl()
                    .and()
                    .contentTypeOptions()
                    .and()
                    .httpStrictTransportSecurity()
                    .and()
                    .xssProtection();

            http.authorizeRequests()
                    //配置安全策略
                    .antMatchers("/api/**").authenticated()//login下请求需要验证
                    .and()
                    .logout()
                    .permitAll()
                    //定义logout不需要验证
                    .and()
                    //使用form表单登录
                    .formLogin();

            http.exceptionHandling().authenticationEntryPoint(customerCasAuthenticationEntryPoint())
                    .and()
                    .addFilter(casAuthenticationFilter())
                    .addFilterBefore(casLogoutFilter(), LogoutFilter.class)
                    .addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class)
            ;

            //禁用CSRF
            http.csrf().disable();

        }


        @Bean
        public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
            ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> servletListenerRegistrationBean = new ServletListenerRegistrationBean<>();
            servletListenerRegistrationBean.setListener(new SingleSignOutHttpSessionListener());
            servletListenerRegistrationBean.setEnabled(true);
            return servletListenerRegistrationBean;
        }

        /**
         * 指定service相关信息
         */
        @Bean
        public ServiceProperties serviceProperties() {
            ServiceProperties serviceProperties = new ServiceProperties();
            serviceProperties.setService(casProperties.getAppServerUrl() + casProperties.getAppLoginUrl());
            serviceProperties.setAuthenticateAllArtifacts(true);
            return serviceProperties;
        }

        /**
         * 认证的入口
         */
        @Bean
        public CustomerCasAuthenticationEntryPoint customerCasAuthenticationEntryPoint() {
            CustomerCasAuthenticationEntryPoint customerCasAuthenticationEntryPoint = new CustomerCasAuthenticationEntryPoint();
            customerCasAuthenticationEntryPoint.setLoginUrl(casProperties.getCasServerLoginUrl());
            customerCasAuthenticationEntryPoint.setServiceProperties(serviceProperties());
            customerCasAuthenticationEntryPoint.setAuthenticationRedirectStrategy(new CasAuthenticationRedirectStrategy());
            return customerCasAuthenticationEntryPoint;
        }

        /**
         * CAS认证过滤器
         */
        @Bean
        public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
            CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
            casAuthenticationFilter.setAuthenticationManager(authenticationManager());
            casAuthenticationFilter.setFilterProcessesUrl(casProperties.getAppLoginUrl());
            casAuthenticationFilter.setAuthenticationSuccessHandler(new CasAuthenticationSuccessHandler());
            return casAuthenticationFilter;
        }

        @Bean
        public Cas30ServiceTicketValidator cas30ServiceTicketValidator() {
            return new Cas30ServiceTicketValidator(casProperties.getCasServerUrl());
        }

        /**
         * cas 认证 Provider
         */
        @Bean
        public CasAuthenticationProvider casAuthenticationProvider() {
            CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
            casAuthenticationProvider.setAuthenticationUserDetailsService(customUserDetailsService());
            //casAuthenticationProvider.setUserDetailsService(customUserDetailsService()); //这里只是接口类型，实现的接口不一样，都可以的。
            casAuthenticationProvider.setServiceProperties(serviceProperties());
            casAuthenticationProvider.setTicketValidator(cas30ServiceTicketValidator());
            casAuthenticationProvider.setKey("casAuthenticationProviderKey");
            return casAuthenticationProvider;
        }

        /**
         * 用户自定义的AuthenticationUserDetailsService
         */
        @Bean
        public AuthenticationUserDetailsService<CasAssertionAuthenticationToken> customUserDetailsService() {
            return new CasUserDetailsService();
        }

        /**
         * 单点登出过滤器
         */
        @Bean
        public SingleSignOutFilter singleSignOutFilter() {
            SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
            singleSignOutFilter.setIgnoreInitConfiguration(true);
            return singleSignOutFilter;
        }

        /**
         * 请求单点退出过滤器
         */
        @Bean
        public LogoutFilter casLogoutFilter() {
            LogoutFilter logoutFilter = new LogoutFilter(casProperties.getCasServerLogoutUrl(), new SecurityContextLogoutHandler());
            logoutFilter.setFilterProcessesUrl(casProperties.getAppLogoutUrl());
            return logoutFilter;
        }

    }

    @Order(3)
    @Configuration
    @ConditionalOnProperty(prefix = "authentication", name = "type", havingValue = "none_authentication", matchIfMissing = false)
    public static class NoneAuthenticationConfiguration extends WebSecurityConfigurerAdapter {

        @Bean
        public Filter noneAuthenticationFilter() {
            return new Filter() {
                @Override
                public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                    SecurityContextHolder.getContext().setAuthentication(new Authentication() {
                        @Override
                        public Collection<? extends GrantedAuthority> getAuthorities() {
                            return null;
                        }

                        @Override
                        public Object getCredentials() {
                            return null;
                        }

                        @Override
                        public Object getDetails() {
                            return null;
                        }

                        @Override
                        public Object getPrincipal() {
                            return null;
                        }

                        @Override
                        public boolean isAuthenticated() {
                            return true;
                        }

                        @Override
                        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

                        }

                        @Override
                        public String getName() {
                            return "wangqimeng";
                        }
                    });
                    chain.doFilter(request, response);
                }
            };
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .anyRequest().permitAll().and().logout().permitAll();
            http.addFilterBefore(noneAuthenticationFilter(), WebAsyncManagerIntegrationFilter.class);
        }
    }
}
