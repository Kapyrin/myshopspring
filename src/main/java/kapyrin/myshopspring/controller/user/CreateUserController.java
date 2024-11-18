package kapyrin.myshopspring.controller.user;

import jakarta.servlet.http.HttpSession;
import kapyrin.myshopspring.entity.Role;
import kapyrin.myshopspring.entity.User;
import kapyrin.myshopspring.service.interfaces.RoleService;
import kapyrin.myshopspring.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CreateUserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final RoleService roleService;


    @PostMapping("/createUser")
    public String createUser(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("address") String address,
            @RequestParam("role") String role,
            HttpSession session) {

        Optional<Role> roleOptional = roleService.getByRoleName(role);

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .address(address)
                .role(roleOptional.get())
                .build();
        userService.add(user);
        session.setAttribute("user", user);

        String redirectURL = getRedirectURL(user.getRole().getUserRole());
        return "redirect:" + redirectURL;
    }

    private String getRedirectURL(String roleName) {
        switch (roleName.toLowerCase()) {
            case "admin":
                return "/admin";
            case "manager":
                return "/managers";
            case "customer":
                return "/customerOrders";
            default:
                return "/";
        }
    }
}
