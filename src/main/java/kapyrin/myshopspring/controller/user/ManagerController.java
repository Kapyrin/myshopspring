package kapyrin.myshopspring.controller.user;

import jakarta.servlet.http.HttpSession;
import kapyrin.myshopspring.entity.*;
import kapyrin.myshopspring.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ManagerController {

    @Autowired
    private UserImplService userService;
    @Autowired
    private ShopOrderImplService shopOrderService;
    @Autowired
    private ProductImplService productService;
    @Autowired
    private OrderStatusImplService orderStatusService;
    @Autowired
    private ProductOrderImplService productOrderService;

    @GetMapping("/managers")
    public String getManagersPage(@RequestParam(value = "action", required = false) String action,
                                  @RequestParam(value = "productId", required = false) Long productId,
                                  Model model, HttpSession session) {

        List<User> onlyCustomers = userService.getAll().stream()
                .filter(user -> "customer".equals(user.getRole().getUserRole()))
                .collect(Collectors.toList());

        Map<Long, List<ShopOrder>> userOrders = new HashMap<>();
        Map<Long, List<Product>> orderProducts = new HashMap<>();

        for (User user : onlyCustomers) {
            List<ShopOrder> orders = shopOrderService.getAllOrdersByUserId(user.getId());
            userOrders.put(user.getId(), orders != null ? orders : new ArrayList<>());
        }

        productOrderService.getAll().forEach(productOrder -> {
            orderProducts
                    .computeIfAbsent(productOrder.getOrder().getId(), k -> new ArrayList<>())
                    .add(productOrder.getProduct());
        });

        List<Product> products = productService.getAll();
        List<OrderStatus> statuses = orderStatusService.getAll();

        if ("filterByProduct".equals(action) && productId != null) {
            List<ShopOrder> filteredOrders = shopOrderService.getOrdersByProductId(productId);
            model.addAttribute("filteredOrders", filteredOrders);
        } else {
            model.addAttribute("allOrders", shopOrderService.getAll());
        }

        model.addAttribute("users", onlyCustomers);
        model.addAttribute("userOrders", userOrders);
        model.addAttribute("orderProducts", orderProducts);
        model.addAttribute("products", products);
        model.addAttribute("statuses", statuses);

        session.setAttribute("users", onlyCustomers);
        session.setAttribute("userOrders", userOrders);

        return "user/manager";
    }
    @PostMapping("/managers")
    public String handlePostActions(@RequestParam("action") String action,
                                    @RequestParam(value = "orderId", required = false) Long orderId,
                                    @RequestParam(value = "statusId", required = false) Integer statusId,
                                    @RequestParam(value = "deleteBeforeDate", required = false) Date deleteBeforeDate) {

        switch (action) {
            case "deleteBeforeDate":
                deleteBeforeDate(deleteBeforeDate);
                break;
            case "updateStatus":
                if (orderId != null && statusId != null) {
                    updateStatus(orderId, statusId);
                }
                break;
            case "closeOrder":
                if (orderId != null) {
                    closeOrder(orderId);
                }
                break;
            default:
                return "redirect:/managers";
        }

        return "redirect:/managers";
    }

    private void closeOrder(Long orderId) {
        shopOrderService.closeOrder(orderId);
    }

    private void updateStatus(Long orderId, int statusId) {
        shopOrderService.updateOrderStatus(orderId, statusId);
    }

    private void deleteBeforeDate(Date deleteBeforeDate) {
        shopOrderService.deleteOrdersBeforeDate(deleteBeforeDate);
    }
}
