package kapyrin.myshopspring.controller.user;

import kapyrin.myshopspring.controller.UtilUsersForController;
import kapyrin.myshopspring.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class LogInOutControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UtilUsersForController utilUsersForController;

    private User adminUser;
    private User managerUser;
    private User customerUser;

    @Test
    void login_AdminUser_RedirectToAdmin() throws Exception {
        adminUser = utilUsersForController.getAdminUser();
        mockMvc.perform(post("/login")
                        .param("email", adminUser.getEmail())
                        .param("password", adminUser.getPassword()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"));
    }

    @Test
    void login_ManagerUser_RedirectToManagers() throws Exception {
        managerUser = utilUsersForController.getManagerUser();
        mockMvc.perform(post("/login")
                        .param("email", managerUser.getEmail())
                        .param("password", managerUser.getPassword()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/managers"));
    }

    @Test
    void login_CustomerUser_RedirectToCustomerOrders() throws Exception {
        customerUser = utilUsersForController.getCustomerUser();
        mockMvc.perform(post("/login")
                        .param("email", customerUser.getEmail())
                        .param("password", customerUser.getPassword()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/customerOrders"));
    }

    @Test
    void login_InvalidUser() throws Exception {
        mockMvc.perform(post("/login")
                        .param("email", "invalid@mail.com")
                        .param("password", "wrong"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/login"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("errorMessage"))
                .andExpect(MockMvcResultMatchers.model().attribute("errorMessage", "Invalid email or password."));
    }

    @Test
    void logout() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    }
}