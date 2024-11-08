package kapyrin.myshopspring.configuration;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kapyrin.myshopspring.entity.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class RoleAccessFilter implements Filter {

    private final Map<String, String> urlRoleMapping = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) {

        urlRoleMapping.put("/admin", "admin");
        urlRoleMapping.put("/customerOrders", "customer");
        urlRoleMapping.put("/createOrder", "customer");
        urlRoleMapping.put("/editProduct", "manager");
        urlRoleMapping.put("/managers", "manager");
        urlRoleMapping.put("/addProduct", "manager");
        urlRoleMapping.put("/products", "manager");
        urlRoleMapping.put("/editUser", "admin");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String requestURI = req.getRequestURI();

        String requiredRole = getRequiredRoleForUrl(requestURI);

        if (requiredRole != null && (user == null || !requiredRole.equals(user.getRole().getUserRole()))) {
            session.setAttribute("errorMessage", "You do not have permission to access this resource. Please login again.");
            res.sendRedirect("/login");
            return;
        }

        chain.doFilter(request, response);
    }

    private String getRequiredRoleForUrl(String requestURI) {
        return urlRoleMapping.entrySet().stream()
                .filter(entry -> requestURI.startsWith(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

}
