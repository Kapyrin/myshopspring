package kapyrin.myshopspring.service.impl;

import kapyrin.myshopspring.AbstractTest;
import kapyrin.myshopspring.entity.*;
import kapyrin.myshopspring.service.interfaces.ProductOrderService;
import kapyrin.myshopspring.service.interfaces.ShopOrderService;
import kapyrin.myshopspring.service.interfaces.UserService;
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
class ProductOrderImplServiceTest extends AbstractTest {

    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    private UserService userService;
    @Autowired
    private OrderStatusImplService orderStatus;
    @Autowired
    ShopOrderService shopOrderService;
    @Autowired
    private ProductImplService productImplService;
    @Autowired
    ServiceTestUtil serviceTestUtil;

    private ProductOrder productOrder;
    private ShopOrder shopOrder;
    private Product product;
    private User customer;
    private OrderStatus status;


    @Test
    void getByIds() {
        Optional<ProductOrder> foundProductOrder = productOrderService.getByIds(product.getId(), shopOrder.getId());
        assertTrue(foundProductOrder.isPresent());

        Optional<ProductOrder> invalidProductOrder = productOrderService.getByIds(100, 100);
        assertFalse(invalidProductOrder.isPresent());
    }

    @Test
    void deleteByIds() {
        Optional<ProductOrder> foundProductOrder = productOrderService.getByIds(product.getId(), shopOrder.getId());
        assertTrue(foundProductOrder.isPresent());

        productOrderService.deleteByIds(product.getId(), shopOrder.getId());
        Optional<ProductOrder> invalidProductOrder = productOrderService.getByIds(product.getId(), shopOrder.getId());
        assertFalse(invalidProductOrder.isPresent());

    }


    @Test
    void update() {
        Optional<ProductOrder> optionalProductOrder = productOrderService.getByIds(product.getId(), shopOrder.getId());
        productOrder = optionalProductOrder.get();
        assertEquals(5, productOrder.getQuantity());

        productOrder.setQuantity(3);
        productOrderService.update(productOrder);
        Optional<ProductOrder> updatedProductOrder = productOrderService.getByIds(product.getId(), shopOrder.getId());
        productOrder = updatedProductOrder.get();
        assertEquals(3, productOrder.getQuantity());
    }

    @Test
    void deleteByEntity() {
        Optional<ProductOrder> foundProductOrder = productOrderService.getByIds(product.getId(), shopOrder.getId());
        assertTrue(foundProductOrder.isPresent());

        productOrderService.deleteByEntity(productOrder);
        Optional<ProductOrder> invalidProductOrder = productOrderService.getByIds(product.getId(), shopOrder.getId());
        assertFalse(invalidProductOrder.isPresent());
    }


    @Test
    void getAll() {
        List<ProductOrder> productOrders = productOrderService.getAll();
        assertTrue(productOrders.size() > 0);
    }

    @Test
    void productFromProductOrder() {
    }

    @Override
    protected void createTestEntity() {
        product = serviceTestUtil.product();
        productImplService.add(product);

        customer = serviceTestUtil.customer();
        userService.add(customer);

        status = serviceTestUtil.orderStatus();
        orderStatus.add(status);

        shopOrder = serviceTestUtil.shopOrder(customer, status);
        shopOrderService.add(shopOrder);

        ProductOrderKey key = new ProductOrderKey(product.getId(), shopOrder.getId());

        productOrder = ProductOrder.builder()
                .id(key)
                .order(shopOrder)
                .product(product)
                .quantity(5)
                .build();

    }

    @Override
    protected void saveTestEntity() {
        productOrderService.add(productOrder);

    }

}