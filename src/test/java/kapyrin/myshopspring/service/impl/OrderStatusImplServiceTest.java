package kapyrin.myshopspring.service.impl;

import kapyrin.myshopspring.AbstractTest;
import kapyrin.myshopspring.entity.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)

class OrderStatusImplServiceTest extends AbstractTest {
    @Autowired
    private OrderStatusImplService orderStatusImplService;
    private OrderStatus orderStatus;

    @Test
    void add() {
        OrderStatus addedOrderStatus = OrderStatus.builder()
                .statusName("addedOrderStatus")
                .build();
        orderStatusImplService.add(addedOrderStatus);

        Long orderStatusId = addedOrderStatus.getId();
        assertNotNull(orderStatusId);
    }

    @Test
    void update() {
        String updatedTestStatus = "updatedTestStatus";
        OrderStatus orderStatus = orderStatusImplService.getById(getTestOrderStatusId()).get();
        assertTrue((orderStatus.getStatusName()).equals("testStatus"));
        orderStatus.setStatusName(updatedTestStatus);

        orderStatusImplService.update(orderStatus);
        Optional<OrderStatus> updatedOrderStatusOptional = orderStatusImplService.getById(getTestOrderStatusId());
        assertTrue((updatedOrderStatusOptional.get().getStatusName()).equals(updatedTestStatus));

    }

    @Test
    void deleteById() {
        Optional<OrderStatus> orderStatusOptional = orderStatusImplService.getById(getTestOrderStatusId());
        assertTrue(orderStatusOptional.isPresent());

        orderStatusImplService.deleteById(getTestOrderStatusId());
        Optional<OrderStatus> deletedOrderStatusOptional = orderStatusImplService.getById(getTestOrderStatusId());
        assertFalse(deletedOrderStatusOptional.isPresent());
    }

    @Test
    void deleteByEntity() {
        Optional<OrderStatus> orderStatusOptional = orderStatusImplService.getById(getTestOrderStatusId());
        assertTrue(orderStatusOptional.isPresent());

        orderStatusImplService.deleteByEntity(orderStatus);
        Optional<OrderStatus> deletedOrderStatusOptional = orderStatusImplService.getById(getTestOrderStatusId());
        assertFalse(deletedOrderStatusOptional.isPresent());
    }

    @Test
    void getAll() {
        List<OrderStatus> orderStatusList = orderStatusImplService.getAll();
        assertTrue(orderStatusList.size() > 0);
    }

    @Test
    void getById() {
        Optional<OrderStatus> orderStatusOptional = orderStatusImplService.getById(getTestOrderStatusId());
        assertTrue(orderStatusOptional.isPresent());

        Optional<OrderStatus> orderStatusOptional2 = orderStatusImplService.getById(100);
        assertFalse(orderStatusOptional2.isPresent());
    }

    @Override
    protected void createTestEntity() {
        orderStatus = OrderStatus.builder()
                .statusName("testStatus")
                .build();
    }

    @Override
    protected void saveTestEntity() {
        orderStatusImplService.add(orderStatus);
    }

    private Long getTestOrderStatusId() {
        return orderStatus.getId();
    }
}