package cn.boommanpro.unifygateway.authentication;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.authentication.AuthenticationRedirectStrategy;


/**
 * @author wangqimeng
 * @date 2019/12/5 16:57
 */

@Slf4j
public final class CasAuthenticationRedirectStrategy implements AuthenticationRedirectStrategy {

    public CasAuthenticationRedirectStrategy() {
    }

    @Override
    public void redirect(HttpServletRequest request, HttpServletResponse response, String potentialRedirectUrl) throws IOException {
        String type = request.getHeader("X-Requested-With") == null ? "" : request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(type)) {
            String origin = request.getHeader("Origin");
            if (origin == null) {
                origin = request.getHeader("Referer");
            }

            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("REDIRECT", "SSOREDIRECT");
            response.setHeader("CONTEXTPATH", potentialRedirectUrl);
            response.setHeader("Access-Control-Allow-Headers", "REDIRECT,CONTEXTPATH");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin,REDIRECT,CONTEXTPATH,Access-Control-Allow-Headers,Access-Control-Allow-Credentials");
            response.setStatus(403);
        } else {
            response.sendRedirect(potentialRedirectUrl);
        }

    }
}
