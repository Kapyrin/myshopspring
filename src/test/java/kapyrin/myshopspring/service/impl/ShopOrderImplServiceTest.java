package kapyrin.myshopspring.service.impl;

import jakarta.persistence.EntityManager;
import kapyrin.myshopspring.AbstractTest;
import kapyrin.myshopspring.entity.*;
import kapyrin.myshopspring.service.interfaces.ShopOrderService;
import kapyrin.myshopspring.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ShopOrderImplServiceTest extends AbstractTest {
    @Autowired
    private UserService userService;
    @Autowired
    ShopOrderService shopOrderService;
    @Autowired
    ProductOrderImplService productOrderService;
    @Autowired
    ServiceTestUtil serviceTestUtil;
    @Autowired
    EntityManager entityManager;

    private User customer;
    private OrderStatus status;
    private ShopOrder shopOrder;
    private ProductOrder productOrder;
    private Product product;
    @Autowired
    private OrderStatusImplService orderStatusImplService;

    @Test
    void update() {
        Optional<ShopOrder> optionalShopOrder = shopOrderService.getById(shopOrder.getId());
        ShopOrder shopOrder = optionalShopOrder.get();
        assertEquals(Date.valueOf(LocalDate.of(2024, 10, 11)), shopOrder.getOrderCreationDate());

        shopOrder.setOrderCreationDate(Date.valueOf(LocalDate.of(2024, 11, 12)));
        shopOrderService.update(shopOrder);
        assertEquals(Date.valueOf(LocalDate.of(2024, 11, 12)), shopOrder.getOrderCreationDate());

    }

    @Test
    void deleteByEntity() {
        Optional<ShopOrder> optionalShopOrder = shopOrderService.getById(shopOrder.getId());
        assertTrue(optionalShopOrder.isPresent());

        shopOrderService.deleteByEntity(shopOrder);
        Optional<ShopOrder> deletedShopOrder = shopOrderService.getById(shopOrder.getId());
        assertFalse(deletedShopOrder.isPresent());

    }

    @Test
    void getAll() {
        List<ShopOrder> shopOrders = shopOrderService.getAll();
        assertTrue(shopOrders.size() > 0);
    }

    @Test
    void getAllOrdersByUserId() {
        List<ShopOrder> ordersByUserId = shopOrderService.getAllOrdersByUserId(customer.getId());
        assertTrue(ordersByUserId.size() > 0);
    }

    @Test
    void getOrdersByProductId() {
        List<ShopOrder> ordersByProductId = shopOrderService.getOrdersByProductId(product.getId());
        assertTrue(ordersByProductId.size() > 0);
    }

    @Test
    void deleteOrdersBeforeDate() {
        List<ShopOrder> orderList = shopOrderService.getAll();
        Date deleteDate = Date.valueOf(LocalDate.of(2024, 11, 11));

        shopOrderService.deleteOrdersBeforeDate(deleteDate);
        List<ShopOrder> deletedOrderList = shopOrderService.getAll();
        assertTrue(orderList.size() > deletedOrderList.size());

    }


    @Test
    void closeOrder() {
        Optional<ShopOrder> optionalShopOrder = shopOrderService.getById(shopOrder.getId());
        ShopOrder shopOrder = optionalShopOrder.get();

        shopOrderService.closeOrder(shopOrder.getId());
        entityManager.refresh(shopOrder);

        ShopOrder closedShopOrder = shopOrderService.getById(shopOrder.getId()).get();
        assertEquals(4L, closedShopOrder.getStatus().getId());
        assertEquals(Date.valueOf(LocalDate.now()), closedShopOrder.getOrderCloseDate());

    }

    @Test
    void updateOrderStatus() {
        Optional<ShopOrder> optionalShopOrder = shopOrderService.getById(shopOrder.getId());
        ShopOrder shopOrder = optionalShopOrder.get();
        long originalOrderId = shopOrder.getStatus().getId();

        shopOrderService.updateOrderStatus(shopOrder.getId(), 2);
        entityManager.refresh(shopOrder);
        assertEquals(2L, shopOrder.getStatus().getId());
        assertNotEquals(originalOrderId, shopOrder.getStatus().getId());

    }

    @Test
    void deleteById() {
        Optional<ShopOrder> optionalShopOrder = shopOrderService.getById(shopOrder.getId());
        assertTrue(optionalShopOrder.isPresent());

        shopOrderService.deleteById(shopOrder.getId());
        Optional<ShopOrder> deletedShopOrder = shopOrderService.getById(shopOrder.getId());
        assertFalse(deletedShopOrder.isPresent());
    }

    @Test
    void getById() {
        Optional<ShopOrder> optionalShopOrder = shopOrderService.getById(shopOrder.getId());
        assertTrue(optionalShopOrder.isPresent());
    }

    @Override
    protected void createTestEntity() {

        customer = serviceTestUtil.customer();
        userService.add(customer);

        status = serviceTestUtil.orderStatus();
        orderStatusImplService.add(status);

        product = serviceTestUtil.product();

        shopOrder = ShopOrder.builder()
                .customer(customer)
                .orderCreationDate(Date.valueOf(LocalDate.of(2024, 10, 11)))
                .status(status)
                .build();
        shopOrderService.add(shopOrder);
        productOrder = serviceTestUtil.productOrder(product, shopOrder, 4);

    }

    @Override
    protected void saveTestEntity() {
        productOrderService.add(productOrder);
    }


}