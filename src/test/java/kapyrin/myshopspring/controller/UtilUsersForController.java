package kapyrin.myshopspring.controller;

import kapyrin.myshopspring.entity.User;
import kapyrin.myshopspring.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Component
public class UtilUsersForController {
    @Autowired
    private UserService userService;
    public User getAdminUser() {
        User user = userService.getById(1L).get();
        return user;
    }

    public User getManagerUser() {
        User user = userService.getById(2L).get();
        return user;
    }

    public User getCustomerUser() {
        User user = userService.getById(3L).get();
        return user;
    }

    public void notAuthenticatedUser(MockMvc mockMvc,String url) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
    }
}
