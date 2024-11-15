package kapyrin.myshopspring.util;

import jakarta.servlet.http.HttpSession;
import kapyrin.myshopspring.entity.OrderStatus;
import kapyrin.myshopspring.entity.ShopOrder;
import kapyrin.myshopspring.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportStringGeneratorTest {

    private ReportStringGenerator reportStringGenerator = new ReportStringGenerator();
    private User user;
    private ShopOrder shopOrder;
    private OrderStatus orderStatus;
    private List<User> userList;
    private List<ShopOrder> shopOrderList;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("First")
                .lastName("Lastt")
                .email("update@email.com")
                .password("password")
                .phoneNumber("6789012456")
                .address("Washington")
                .build();

        orderStatus = OrderStatus.builder()
                .statusName("testStatus")
                .build();


        shopOrder = ShopOrder.builder()
                .customer(user)
                .status(orderStatus)
                .orderCreationDate(Date.valueOf(LocalDate.of(2024, 11, 11)))
                .build();

        userList = List.of(user);

        shopOrderList = List.of(shopOrder);

        session = Mockito.mock(HttpSession.class);
    }

    @Test
    void fromUserList() {
        String reportStringUserList = reportStringGenerator.fromUserList(userList);

        String expectedEmail = "update@email.com";
        String expectedPhoneNumber = "6789012456";

        assertTrue(reportStringUserList.contains(expectedEmail));
        assertTrue(reportStringUserList.contains(expectedPhoneNumber));

    }

    @Test
    void fromUsersOrders() {
        shopOrderList = List.of(shopOrder);

        Map<Long, List<ShopOrder>> userOrders = new HashMap<>();
        userOrders.put(1L, shopOrderList);

        String reportStringUserOrders = reportStringGenerator.fromUsersOrders(userList, userOrders);
        String expectedOrderStatus = "testStatus";
        assertTrue(reportStringUserOrders.contains(expectedOrderStatus));

    }

    @Test
    void fromUserOrders() {
        String reportStringFromUserOrders = reportStringGenerator.fromUserOrders(shopOrderList, session);
        String expectedDate = "2024-11-11";
        assertTrue(reportStringFromUserOrders.contains(expectedDate));
    }
}