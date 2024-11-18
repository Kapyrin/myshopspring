package kapyrin.myshopspring.controller.user;

import kapyrin.myshopspring.controller.UtilUsersForController;
import kapyrin.myshopspring.entity.OrderStatus;
import kapyrin.myshopspring.entity.ShopOrder;
import kapyrin.myshopspring.entity.User;
import kapyrin.myshopspring.service.impl.OrderStatusImplService;
import kapyrin.myshopspring.service.impl.ServiceTestUtil;
import kapyrin.myshopspring.service.interfaces.ShopOrderService;
import kapyrin.myshopspring.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ManagerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ShopOrderService shopOrderService;

    @Autowired
    OrderStatusImplService orderStatusService;
    @Autowired
    UtilUsersForController utilUsersForController;
    @Autowired
    ServiceTestUtil serviceTestUtil;

    private User userManager;
    private User customer;
    private ShopOrder shopOrder;
    private OrderStatus status;


    private final String URL_MANAGERS = "/managers";

    @Test
    void getManagersPage() throws Exception {
        utilUsersForController.notAuthenticatedUser(mockMvc, URL_MANAGERS);

        userManager = utilUsersForController.getManagerUser();
        mockMvc.perform(get(URL_MANAGERS)
                        .with(SecurityMockMvcRequestPostProcessors.user(userManager.getEmail()).roles("MANAGER"))
                        .sessionAttr("user", userManager))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/manager"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("users"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("userOrders"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("orderProducts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("products"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("statuses"));
    }

    @Test
    void handlePostActionsDeleteBeforeDate() throws Exception {
        utilUsersForController.notAuthenticatedUser(mockMvc, URL_MANAGERS);

        userManager = utilUsersForController.getManagerUser();

        prepareOrder();

        List<ShopOrder> orderList = shopOrderService.getAll();
        Date deleteDate = Date.valueOf(LocalDate.of(2024, 11, 11));

        mockMvc.perform(post("/managers")
                        .with(SecurityMockMvcRequestPostProcessors.user(userManager.getEmail()).roles("MANAGER"))
                        .sessionAttr("user", userManager)
                        .param("action", "deleteBeforeDate")
                        .param("deleteBeforeDate", deleteDate.toString()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(URL_MANAGERS));

        List<ShopOrder> deletedOrderList = shopOrderService.getAll();
        assertTrue(orderList.size() > deletedOrderList.size());

    }

    @Test
    void handlePOstActionUpdateStatus() throws Exception {
        utilUsersForController.notAuthenticatedUser(mockMvc, URL_MANAGERS);

        userManager = utilUsersForController.getManagerUser();
        prepareOrder();

        long currentStatusId = shopOrder.getStatus().getId();
        long newStatusId = 2L;
        assertNotEquals(currentStatusId, newStatusId);

        mockMvc.perform(post(URL_MANAGERS)
                        .with(SecurityMockMvcRequestPostProcessors.user(userManager.getEmail()).roles("MANAGER"))
                        .sessionAttr("user", userManager)
                        .param("action", "updateStatus")
                        .param("orderId", shopOrder.getId().toString())
                        .param("statusId", String.valueOf(newStatusId)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(URL_MANAGERS));

        ShopOrder updatedOrder = shopOrderService.getById(shopOrder.getId()).orElseThrow();
        assertEquals(newStatusId, updatedOrder.getStatus().getId());

    }

    @Test
    void handlePostActionsCloseOrder() throws Exception {
        utilUsersForController.notAuthenticatedUser(mockMvc, URL_MANAGERS);

        userManager = utilUsersForController.getManagerUser();
        prepareOrder();

        Optional<ShopOrder> optionalShopOrder = shopOrderService.getById(shopOrder.getId());
        ShopOrder shopOrder = optionalShopOrder.get();
        assertNotEquals(4l, shopOrder.getStatus().getId());

        mockMvc.perform(post(URL_MANAGERS)
                        .with(SecurityMockMvcRequestPostProcessors.user(userManager.getEmail()).roles("MANAGER"))
                        .sessionAttr("user", userManager)
                        .param("action", "closeOrder")
                        .param("orderId", shopOrder.getId().toString()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(URL_MANAGERS));

        ShopOrder closedShopOrder = shopOrderService.getById(shopOrder.getId()).get();
        assertEquals(4L, closedShopOrder.getStatus().getId());
        assertEquals(Date.valueOf(LocalDate.now()), closedShopOrder.getOrderCloseDate());
    }

    private void prepareOrder() {
        customer = serviceTestUtil.customer();
        userService.add(customer);

        status = serviceTestUtil.orderStatus();
        orderStatusService.add(status);

        shopOrder = ShopOrder.builder()
                .customer(customer)
                .orderCreationDate(Date.valueOf(LocalDate.of(2024, 10, 11)))
                .status(status)
                .build();
        shopOrderService.add(shopOrder);

    }

}