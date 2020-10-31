package web.springbootcrud.config.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import web.springbootcrud.model.User;
import web.springbootcrud.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {
    // Spring Security использует объект Authentication, пользователя авторизованной сессии.

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        User user = userService.getUserByName(authentication.getName());

        if (roles.contains("ROLE_ADMIN")) {
            httpServletResponse.sendRedirect("/adm/users");
        } else {
            String redirectUrl = String.format("/UserProfile/%d", user.getId());
            httpServletResponse.sendRedirect(redirectUrl);
        }
    }
}
