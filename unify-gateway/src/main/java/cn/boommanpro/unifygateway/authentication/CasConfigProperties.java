package cn.boommanpro.unifygateway.authentication;

import lombok.Data;

/**
 * @author wangqimeng
 * @date 2019/12/5 11:09
 */
@Data
public class CasConfigProperties {

    /**
     * CAS服务登录地址
     */

    private String casServerUrl;

    /**
     * CAS服务登录地址
     */
    private String casServerLoginUrl;

    /**
     * CAS服务登出地址
     */
    private String casServerLogoutUrl;

    /**
     * app地址
     */
    private String appServerUrl;

    /**
     * app 登录地址
     */
    private String appLoginUrl;

    /**
     * app登出地址
     */
    private String appLogoutUrl;

}
