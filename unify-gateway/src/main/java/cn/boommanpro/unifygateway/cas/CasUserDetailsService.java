package cn.boommanpro.unifygateway.cas;

import java.util.Map;

import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 用于加载用户信息 实现UserDetailsService接口，或者实现AuthenticationUserDetailsService接口
 */
public class CasUserDetailsService implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {
    @Override
    public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
        // 结合具体的逻辑去实现用户认证，并返回继承UserDetails的用户对象;
        System.out.println("当前的用户名是："+token.getName());

        //获取用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(token.getName());
        userInfo.setRole("ROLE_USER");
        Map<String, Object> userAttributess = token.getAssertion().getPrincipal().getAttributes();
        //System.out.println(userAttributess.toString());
        if (userAttributess != null) {
            userInfo.setId( String.valueOf(userAttributess.get("id")));
            userInfo.setNickname( String.valueOf(userAttributess.get("nickname")));
            userInfo.setRealName( String.valueOf(userAttributess.get("real_name")));
            userInfo.setEmail( String.valueOf(userAttributess.get("email")));
            userInfo.setCountryCode( String.valueOf(userAttributess.get("country_code")));
        }

        System.out.println(userInfo.toString());
        return userInfo;
    }

}