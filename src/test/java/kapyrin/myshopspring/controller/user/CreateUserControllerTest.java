package kapyrin.myshopspring.controller.user;

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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)

class CreateUserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;


    @Test
    void createUser() throws Exception {
        mockMvc.perform(post("/createUser")
                        .param("firstName", "Create")
                        .param("lastName", "User")
                        .param("email", "create@user.com")
                        .param("password", "password")
                        .param("phoneNumber", "+345043435")
                        .param("address", "city")
                        .param("role", "customer"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/customerOrders"));

        Optional<User> createdUser = userService.authenticate("create@user.com", "password");
        assertTrue(createdUser.isPresent());
        assertThat(createdUser.get().getFirstName()).isEqualTo("Create");
        assertThat(createdUser.get().getLastName()).isEqualTo("User");
        assertThat(createdUser.get().getEmail()).isEqualTo("create@user.com");
        assertThat(createdUser.get().getPassword()).isEqualTo("password");
        assertThat(createdUser.get().getPhoneNumber()).isEqualTo("+345043435");
        assertThat(createdUser.get().getAddress()).isEqualTo("city");
    }
}