package kapyrin.myshopspring.service.impl;

import kapyrin.myshopspring.entity.*;
import kapyrin.myshopspring.service.interfaces.ProductOrderService;
import kapyrin.myshopspring.service.interfaces.RoleService;
import kapyrin.myshopspring.service.interfaces.ShopOrderService;
import kapyrin.myshopspring.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;

@Component
public class ServiceTestUtil {
    @Autowired
    private ProductOrderService productOrderService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderStatusImplService orderStatusService;
    @Autowired
    private ShopOrderService shopOrderService;
    @Autowired
    private ProductImplService productImplService;


    public User customer() {
        User customer = User.builder()
                .firstName("Customer")
                .lastName("User")
                .email("bla@bla.com")
                .password("password")
                .phoneNumber("+7999324023")
                .address("Tver")
                .role(roleService.getByRoleName("manager").get())
                .build();

        userService.add(customer);
        return customer;
    }

    public ShopOrder shopOrder(User customer, OrderStatus status) {
        ShopOrder shopOrder = ShopOrder.builder()
                .customer(customer)
                .orderCreationDate(Date.valueOf(LocalDate.of(2024, 11, 11)))
                .status(status)
                .build();

        shopOrderService.add(shopOrder);
        return shopOrder;
    }

    public OrderStatus orderStatus() {
        OrderStatus status = OrderStatus.builder()
                .statusName("testStatus")
                .build();

        orderStatusService.add(status);
        return status;
    }

    public Product product() {

        Product product = Product.builder()
                .productName("testProduct")
                .productDescription("descriptionOfTestProduct")
                .price(600.0)
                .productRemain(10)
                .build();
        productImplService.add(product);
        return product;
    }

    public ProductOrder productOrder(Product product, ShopOrder shopOrder, int quantity) {
        ProductOrder productOrder = ProductOrder.builder()
                .id(new ProductOrderKey(product.getId(), shopOrder.getId()))
                .order(shopOrder)
                .product(product)
                .quantity(quantity)
                .build();
        productOrderService.add(productOrder);
        return productOrder;
    }

}
