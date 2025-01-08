package kapyrin.myshopspring.controller.util;

import kapyrin.myshopspring.controller.UtilUsersForController;
import kapyrin.myshopspring.entity.OrderStatus;
import kapyrin.myshopspring.entity.ShopOrder;
import kapyrin.myshopspring.entity.User;
import kapyrin.myshopspring.service.impl.ServiceTestUtil;
import kapyrin.myshopspring.service.interfaces.ShopOrderService;
import kapyrin.myshopspring.service.interfaces.UserService;
import kapyrin.myshopspring.util.ReportStringGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;
    @Autowired
    private ShopOrderService shopOrderService;
    @Autowired
    UtilUsersForController utilUsersForController;
    @Autowired
    ServiceTestUtil serviceTestUtil;


    private User user;
    private ShopOrder shopOrder;
    private OrderStatus orderStatus;

    private final String URL_REPORT = "/report";

    @Test
    void generateUsersOrdersReport() throws Exception {
        List<User> customers = userService.getAll().stream()
                .filter(user -> "customer".equals(user.getRole().getUserRole()))
                .toList();

        Map<Long, List<ShopOrder>> userOrders = new HashMap<>();
        for (User user : customers) {
            userOrders.put(user.getId(), shopOrderService.getAllOrdersByUserId(user.getId()));
        }


        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String expectedFileName = "userOrdersReport" + currentDate + ".txt";
        user = utilUsersForController.getManagerUser();
        mockMvc.perform(get(URL_REPORT)
                        .with(SecurityMockMvcRequestPostProcessors.user(user.getEmail()).roles("MANAGER"))
                        .param("reportType", "usersOrders")
                        .sessionAttr("users", customers)
                        .sessionAttr("userOrders", userOrders))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + expectedFileName + "\""))
                .andExpect(content().contentType(MediaType.TEXT_PLAIN));
    }

    @Test
    void generatedAllUsersReport() throws Exception {
        List<User> allUsers = userService.getAll();

        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String expectedFileName = "allUsersReport" + currentDate + ".txt";

        user = utilUsersForController.getAdminUser();
        mockMvc.perform(get(URL_REPORT)
                        .with(SecurityMockMvcRequestPostProcessors.user(user.getEmail()).roles("ADMIN"))
                        .param("reportType", "allUsers")
                        .sessionAttr("users", allUsers))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + expectedFileName + "\""))
                .andExpect(content().contentType(MediaType.TEXT_PLAIN));
    }

    @Test
    void generateUserOrdersReport() throws Exception {
        user = utilUsersForController.getCustomerUser();
        orderStatus = serviceTestUtil.orderStatus();
        shopOrder = serviceTestUtil.shopOrder(user, orderStatus);
        shopOrderService.add(shopOrder);

        List<ShopOrder> orders = shopOrderService.getAllOrdersByUserId(user.getId());

        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String expectedFileName = "userPersonalOrdersReport" + currentDate + ".txt";

        user = utilUsersForController.getCustomerUser();
        mockMvc.perform(get(URL_REPORT)
                        .with(SecurityMockMvcRequestPostProcessors.user(user.getEmail()).roles("CUSTOMER"))
                        .param("reportType", "userOrders")
                        .sessionAttr("orders", orders))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + expectedFileName + "\""))
                .andExpect(content().contentType(MediaType.TEXT_PLAIN));
    }
}