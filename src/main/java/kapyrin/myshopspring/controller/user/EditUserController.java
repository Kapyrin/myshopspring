package kapyrin.myshopspring.controller.user;

import kapyrin.myshopspring.entity.Role;
import kapyrin.myshopspring.entity.User;
import kapyrin.myshopspring.service.impl.RoleImplService;
import kapyrin.myshopspring.service.impl.UserImplService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.Optional;

@Controller
public class EditUserController {

    private final UserImplService userService;
    private final RoleImplService roleService;


    @Autowired
    public EditUserController(UserImplService userService, RoleImplService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping("/editUser")
    public String editUserForm(@RequestParam("userId") Long userId, Model model) {
        Optional<User> user = userService.getById(userId);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/editUser";
        } else {
            return "redirect:/admin";
        }
    }

    @PostMapping("/editUser")
    public String editUserSubmit(
            @RequestParam("userId") Long userId,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("address") String address,
            @RequestParam("role") String roleParam) {
        Optional<Role> roleOptional = roleService.getByRoleName(roleParam);
        if (roleOptional.isPresent()) {

            User updatedUser = User.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .password(password)
                    .phoneNumber(phoneNumber)
                    .address(address)
                    .role(roleOptional.get())
                    .id(userId)
                    .build();
            userService.update(updatedUser);
        }

        return "redirect:/admin";
    }
}
