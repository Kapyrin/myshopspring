package kapyrin.myshopspring.controller.product;

import kapyrin.myshopspring.controller.UtilUsersForController;
import kapyrin.myshopspring.entity.Product;
import kapyrin.myshopspring.entity.User;
import kapyrin.myshopspring.service.impl.ProductImplService;
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
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductImplService productService;
    @Autowired
    UtilUsersForController utilUsersForController;

    private User user;
    private Product product;
    private final String URL_GET_PRODUCT = "/products";
    private final String URL_ADD_PRODUCT = "/addProduct";
    private final String URL_EDIT_PRODUCT = "/editProduct";

    @Test
    void showProducts() throws Exception {
        utilUsersForController.notAuthenticatedUser(mockMvc, URL_GET_PRODUCT);

        user = utilUsersForController.getManagerUser();
        mockMvc.perform(get("/products").sessionAttr("user", user))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("product/productManagement"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("products"));

    }

    @Test
    void showAddProductForm() throws Exception {
        utilUsersForController.notAuthenticatedUser(mockMvc, URL_GET_PRODUCT);

        user = utilUsersForController.getManagerUser();
        mockMvc.perform(get(URL_ADD_PRODUCT).sessionAttr("user", user))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("product/addProduct"));


    }

    @Test
    void addProduct() throws Exception {
        utilUsersForController.notAuthenticatedUser(mockMvc, URL_GET_PRODUCT);

        User user = utilUsersForController.getManagerUser();
        mockMvc.perform(post(URL_ADD_PRODUCT).sessionAttr("user", user)
                        .param("productName", "Test Product")
                        .param("productDescription", "Test Description")
                        .param("productPrice", "2500.0")
                        .param("productQuantity", "50"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(URL_GET_PRODUCT));

        Product addedProduct = productService.getAll().stream()
                .filter(product -> "Test Product".equals(product.getProductName()))
                .findFirst().
                orElse(null);
        assertThat(addedProduct).isNotNull();
        assertThat(addedProduct.getPrice()).isEqualTo(2500.0);
        assertThat(addedProduct.getProductRemain()).isEqualTo(50);
    }

    @Test
    void showEditProductForm() throws Exception {
        utilUsersForController.notAuthenticatedUser(mockMvc, URL_GET_PRODUCT);

        user = utilUsersForController.getManagerUser();
        product = productService.getById(1L).get();

        mockMvc.perform(get(URL_EDIT_PRODUCT)
                        .sessionAttr("user", user)
                        .param("id", product.getId().toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("product/editProduct"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("product"));
    }


    @Test
    void editProduct() throws Exception {
        utilUsersForController.notAuthenticatedUser(mockMvc, URL_EDIT_PRODUCT);

        User user = utilUsersForController.getManagerUser();
        Product product = productService.getById(1L).get();

        mockMvc.perform(post("/editProduct").sessionAttr("user", user)
                        .param("productId", product.getId().toString())
                        .param("productName", "Updated Product")
                        .param("productDescription", "Updated Description")
                        .param("productPrice", "10000.0")
                        .param("productRemain", "25"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(URL_GET_PRODUCT));

        Product editedProduct = productService.getById(1L).get();
        assertThat(editedProduct).isNotNull();
        assertThat(editedProduct.getProductName()).isEqualTo("Updated Product");
        assertThat(editedProduct.getProductDescription()).isEqualTo("Updated Description");
        assertThat(editedProduct.getPrice()).isEqualTo(10000.0);
        assertThat(editedProduct.getProductRemain()).isEqualTo(25);
    }


    @Test
    void deleteProduct() throws Exception {
        utilUsersForController.notAuthenticatedUser(mockMvc, URL_GET_PRODUCT);

        User user = utilUsersForController.getManagerUser();
        Product product = productService.getById(1L).get();
        mockMvc.perform(post(URL_GET_PRODUCT).sessionAttr("user", user)
                        .param("productId", product.getId().toString())
                        .param("action", "delete"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(URL_GET_PRODUCT));

        assertThat(productService.getById(1L)).isEmpty();

    }
}