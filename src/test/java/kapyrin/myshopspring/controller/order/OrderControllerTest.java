package kapyrin.myshopspring.controller.order;

import kapyrin.myshopspring.controller.UtilUsersForController;
import kapyrin.myshopspring.entity.ShopOrder;
import kapyrin.myshopspring.entity.User;
import kapyrin.myshopspring.service.interfaces.ShopOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ShopOrderService shopOrderService;
    @Autowired
    UtilUsersForController utilUsersForController;
    private User user;
    private final String URL_CREATE_ORDER = "/createOrder";
    private final String URL_CUSTOMER_ORDER = "/customerOrders";

    @Test
    void showCreateOrderPage() throws Exception {
        utilUsersForController.notAuthenticatedUser(mockMvc, URL_CREATE_ORDER);

        user = utilUsersForController.getCustomerUser();
        mockMvc.perform(get(URL_CREATE_ORDER).sessionAttr("user", user))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order/createOrder"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("products"));

    }

    @Test
    void createOrder() throws Exception {
        utilUsersForController.notAuthenticatedUser(mockMvc, URL_CREATE_ORDER);

        user = utilUsersForController.getCustomerUser();
        List<ShopOrder> customerOrdersBeforeAddOrder = shopOrderService.getAllOrdersByUserId(user.getId());
        mockMvc.perform(post(URL_CREATE_ORDER)
                        .sessionAttr("user", user)
                        .param("quantity1", "1")
                        .param("quantity2", "2"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/customerOrders"));

        List<ShopOrder> customerOrdersAfterAddOrder = shopOrderService.getAllOrdersByUserId(user.getId());
        assertTrue(customerOrdersAfterAddOrder.size() > customerOrdersBeforeAddOrder.size());

    }

    @Test
    void showCustomerOrders() throws Exception {
        utilUsersForController.notAuthenticatedUser(mockMvc, URL_CUSTOMER_ORDER);

        user = utilUsersForController.getCustomerUser();
        mockMvc.perform(get(URL_CUSTOMER_ORDER).sessionAttr("user", user))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order/customerOrders"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("orders"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("orderTotalAmounts"));

    }


}