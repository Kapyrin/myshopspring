package kapyrin.myshopspring.controller.order;


import jakarta.servlet.http.HttpSession;
import kapyrin.myshopspring.entity.*;
import kapyrin.myshopspring.exception.entity.OrderStatusException;
import kapyrin.myshopspring.service.impl.OrderStatusImplService;
import kapyrin.myshopspring.service.impl.ProductImplService;
import kapyrin.myshopspring.service.impl.ProductOrderImplService;
import kapyrin.myshopspring.service.impl.ShopOrderImplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class OrderController {

    @Autowired
    private ProductImplService productService;
    @Autowired
    private ShopOrderImplService shopOrderService;
    @Autowired
    private ProductOrderImplService productOrderService;
    @Autowired
    private OrderStatusImplService orderStatusService;

    @GetMapping("/createOrder")
    public String showCreateOrderPage(Model model) {
        List<Product> products = productService.getAll();
        model.addAttribute("products", products);
        return "order/createOrder";
    }

    @PostMapping("/createOrder")
    public String createOrder(@SessionAttribute("user") User user,
                              @RequestParam Map<String, String> allParams) {
        if (user == null) {
            return "redirect:/login";
        }

        OrderStatus orderStatus = orderStatusService.getById(1)
                .orElseThrow(() -> new OrderStatusException("Order status not found"));

        ShopOrder order = ShopOrder.builder()
                .customer(user)
                .orderCreationDate(new Date(System.currentTimeMillis()))
                .status(orderStatus)
                .build();
        shopOrderService.add(order);

        productService.getAll().forEach(product -> {
            String quantityParam = allParams.get("quantity" + product.getId());
            if (quantityParam != null && !quantityParam.isEmpty()) {
                int quantity = Integer.parseInt(quantityParam);
                if (quantity > 0) {
                    ProductOrder productOrder = ProductOrder.builder()
                            .id(new ProductOrderKey(order.getId(), product.getId()))
                            .order(order)
                            .product(product)
                            .quantity(quantity)
                            .build();
                    productOrderService.add(productOrder);
                }
            }
        });

        return "redirect:/customerOrders";
    }
    @GetMapping("/customerOrders")
    public String showCustomerOrders(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<ShopOrder> orders = shopOrderService.getAllOrdersByUserId(loggedInUser.getId());
        Map<ShopOrder, Double> orderTotalAmounts = calculateTotalAmountForOrders(orders);

        session.setAttribute("orders", orders);
        session.setAttribute("orderTotalAmounts", orderTotalAmounts);

        model.addAttribute("user", loggedInUser);
        model.addAttribute("orders", orders);
        model.addAttribute("orderTotalAmounts", orderTotalAmounts);

        return "order/customerOrders";
    }

    private Map<ShopOrder, Double> calculateTotalAmountForOrders(List<ShopOrder> orders) {
        Map<ShopOrder, Double> orderTotalAmounts = new HashMap<>();
        List<ProductOrder> allProductOrders = productOrderService.getAll();

        for (ShopOrder order : orders) {
            double totalAmount = 0;

            List<ProductOrder> productOrdersForOrder = allProductOrders.stream()
                    .filter(po -> po.getOrder().getId().equals(order.getId()))
                    .collect(Collectors.toList());

            for (ProductOrder productOrder : productOrdersForOrder) {
                totalAmount += productOrder.getProduct().getPrice() * productOrder.getQuantity();
            }

            orderTotalAmounts.put(order, totalAmount);
        }
        return orderTotalAmounts;
    }
}