package kapyrin.myshopspring.controller.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LanguageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void changeLanguage() throws Exception {
        mockMvc.perform(get("/language").param("lang", "en"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

    }
}