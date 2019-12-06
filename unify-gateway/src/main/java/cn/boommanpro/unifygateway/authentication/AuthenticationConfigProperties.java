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


}
