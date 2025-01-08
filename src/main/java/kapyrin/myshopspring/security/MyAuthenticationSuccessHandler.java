package kapyrin.myshopspring.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(authority -> authority.getAuthority())
                .orElse("ROLE_UNKNOWN");
        log.info("Authentication success with role:: " + role);

        switch (role) {
            case "ROLE_ADMIN" -> response.sendRedirect("/admin");
            case "ROLE_MANAGER" -> response.sendRedirect("/managers");
            case "ROLE_CUSTOMER" -> response.sendRedirect("/customerOrders");
            default -> response.sendRedirect("/");
        }
    }
}
