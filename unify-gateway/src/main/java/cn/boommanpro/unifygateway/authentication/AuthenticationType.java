package cn.boommanpro.unifygateway.authentication;

/**
 * 认证类型
 *
 * @author wangqimeng
 * @date 2019/12/4 19:16
 */
public enum AuthenticationType {
    /**
     * CAS认证
     */
    CAS_AUTHENTICATION,
    /**
     * 普通认证
     */
    NORMAL_AUTHENTICATION,
    /**
     * 不进行认证
     */
    NONE_AUTHENTICATION,
    ;

}
