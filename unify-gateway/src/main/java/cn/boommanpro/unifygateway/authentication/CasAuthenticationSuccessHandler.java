package cn.boommanpro.unifygateway.authentication;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * @author wangqimeng
 * @date 2019/12/5 18:35
 */
public class CasAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * 做Success 重定向
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object location = request.getSession().getAttribute(CasConstant.REDIRECT_URL);
        request.getSession().removeAttribute(CasConstant.REDIRECT_URL);
        if (location != null) {
            response.sendRedirect(location.toString());
        }
    }
}
