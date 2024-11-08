package kapyrin.myshopspring.controller.user;

import kapyrin.myshopspring.entity.User;
import kapyrin.myshopspring.service.impl.UserImplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

@Controller
public class LogInOutController {

    @Autowired
    private UserImplService userService;

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        Optional<User> user = userService.authenticate(email, password);
        if (user.isPresent()) {
            User userDB = user.get();
            session.setAttribute("user", userDB);


            String userRole = userDB.getRole().getUserRole();
            switch (userRole) {
                case "admin":
                    return "redirect:/admin";
                case "manager":
                    return "redirect:/managers";
                case "customer":
                    return "redirect:/customerOrders";
                default:
                    return "redirect:/";
            }
        } else {
            model.addAttribute("errorMessage", "Invalid email or password.");
            return "user/login";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }
}
