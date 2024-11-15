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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class EditUserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UtilUsersForController utilUsersForController;
    private final String URL_EDIT_USER = "/editUser";

    private User user;


    @Test
    void editUserForm() throws Exception {
        utilUsersForController.notAuthenticatedUser(mockMvc, URL_EDIT_USER);

        user = utilUsersForController.getAdminUser();
        mockMvc.perform(get(URL_EDIT_USER)
                        .sessionAttr("user", user)
                        .param("userId", user.getId().toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/editUser"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", user));

        mockMvc.perform(get(URL_EDIT_USER)
                        .sessionAttr("user", user)
                        .param("userId", "100"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"));

    }

    @Test
    void editUserSubmit() throws Exception {
        utilUsersForController.notAuthenticatedUser(mockMvc, URL_EDIT_USER);

        user = utilUsersForController.getAdminUser();
        mockMvc.perform(post("/editUser")
                        .sessionAttr("user", user)
                        .param("userId", user.getId().toString())
                        .param("firstName", "UpdatedFirstName")
                        .param("lastName", "UpdatedLastName")
                        .param("email", "updated@user.com")
                        .param("password", "password")
                        .param("phoneNumber", "+79036443344")
                        .param("address", "Gotem City")
                        .param("role", "admin"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"));


        Optional<User> updatedUser = userService.getById(user.getId());
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getFirstName()).isEqualTo("UpdatedFirstName");
        assertThat(updatedUser.get().getLastName()).isEqualTo("UpdatedLastName");
        assertThat(updatedUser.get().getEmail()).isEqualTo("updated@user.com");
        assertThat(updatedUser.get().getPassword()).isEqualTo("password");
        assertThat(updatedUser.get().getPhoneNumber()).isEqualTo("+79036443344");
        assertThat(updatedUser.get().getAddress()).isEqualTo("Gotem City");
    }
}