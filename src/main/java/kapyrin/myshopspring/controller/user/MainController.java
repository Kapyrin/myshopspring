package kapyrin.myshopspring.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage(){
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