package cn.boommanpro.unifygateway.authentication;

import lombok.Data;

/**
 * @author wangqimeng
 * @date 2019/12/4 19:19
 */
@Data
public class AuthenticationConfigProperties {

    /**
     * 默认普通登录
     */
    private AuthenticationType type = AuthenticationType.NORMAL_AUTHENTICATION;

    /**
     * 是否开启debug模式
     * 默认false
     *
     * 开启方式有
     * 1.
     * logging.level:
     *   org.springframework.security: debug
     * 2.EnableWebSecurity(debug=true)
     *
     * 3.调用WebSecurity.debug(true);
     *
     * 本方法采用的3
     */
    private boolean debug = false;


}
