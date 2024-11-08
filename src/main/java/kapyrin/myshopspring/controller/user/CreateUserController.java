package kapyrin.myshopspring.controller.user;

import kapyrin.myshopspring.entity.Role;
import kapyrin.myshopspring.entity.User;
import kapyrin.myshopspring.service.impl.RoleImplService;
import kapyrin.myshopspring.service.impl.UserImplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import java.util.Optional;

@Controller
public class CreateUserController {

    @Autowired
    private final UserImplService userService;
    @Autowired
    private final  RoleImplService roleService;

    public CreateUserController(UserImplService userService, RoleImplService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

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
