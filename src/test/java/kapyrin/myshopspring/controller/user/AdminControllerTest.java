package kapyrin.myshopspring.controller.user;

import kapyrin.myshopspring.controller.UtilUsersForController;
import kapyrin.myshopspring.entity.User;
import kapyrin.myshopspring.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;
    @Autowired
    UtilUsersForController utilUsersForController;

    private User user;

    private final String URL_ADMIN = "/admin";
    private final String URL_FIND_BY_ID = "/findUserById";
    private final String URL_DELETE_USER = "/deleteUser";

    @Test
    void showAdminPage() throws Exception {
        utilUsersForController.notAuthenticatedUser(mockMvc, URL_ADMIN);

        user = utilUsersForController.getAdminUser();
        mockMvc.perform(get(URL_ADMIN).sessionAttr("user", user))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/admin"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("users"));
    }

    @Test
    void findUserById() throws Exception {
        utilUsersForController.notAuthenticatedUser(mockMvc, URL_FIND_BY_ID);

        user = utilUsersForController.getAdminUser();
        mockMvc.perform(get(URL_FIND_BY_ID)
                        .sessionAttr("user", user)
                        .param("userId", user.getId().toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/admin"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("foundUser"));
    }

    @Test
    void deleteUser() throws Exception {
        utilUsersForController.notAuthenticatedUser(mockMvc, URL_DELETE_USER);

        user = utilUsersForController.getAdminUser();
        mockMvc.perform(get(URL_DELETE_USER)
                        .sessionAttr("user", user)
                        .param("userId", user.getId().toString()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"));

        Optional<User> deletedUser = userService.getById(user.getId());
        assertThat(deletedUser).isEmpty();
    }

}