package kapyrin.myshopspring.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Locale;

@Controller
public class MainController {
    @Autowired
    private MessageSource messageSource;
    @GetMapping("/")
    public String mainPage(Model model, Locale locale) {
         return "index";
    }



    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

    @GetMapping("/createUser")
    public String createUserPage() {
        return "user/createUser";
    }
}