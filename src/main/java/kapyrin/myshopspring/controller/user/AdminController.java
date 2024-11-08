package kapyrin.myshopspring.controller.user;

import jakarta.servlet.http.HttpSession;
import kapyrin.myshopspring.entity.User;
import kapyrin.myshopspring.service.impl.UserImplService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    private UserImplService userService;

    @GetMapping("/admin")
    public String showAdminPage(Model model, HttpSession session) {
        List<User> users = userService.getAll();
        model.addAttribute("users", users);
        session.setAttribute("users", users);
        return "user/admin";
    }

    @GetMapping("/findUserById")
    public String findUserById(@RequestParam("userId") Long userId, Model model, HttpSession session) {
        List<User> users = (List<User>) session.getAttribute("users");
        model.addAttribute("users", users);
        Optional<User> user = userService.getById(userId);
        if (user.isPresent()) {
            model.addAttribute("foundUser", user.get());
        } else {
            model.addAttribute("errorMessage", "User not found.");
        }
        return "user/admin";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") Long userId) {
        userService.deleteById(userId);
        return "redirect:/admin";
    }


}
