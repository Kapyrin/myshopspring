package kapyrin.myshopspring.util;

import jakarta.servlet.http.HttpSession;
import kapyrin.myshopspring.entity.ShopOrder;
import kapyrin.myshopspring.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ReportStringGenerator {

    public String fromUserList(List<User> userList) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%-5s %-15s %-15s %-30s %-15s %-20s %-20s%n",
                "ID", "First Name", "Last Name", "Email", "Password", "Phone Number", "Address"));
        builder.append("---------------------------------------------------------------------------------------------------\n");

        for (User user : userList) {
            builder.append(String.format("%-5d %-15s %-15s %-30s %-15s %-20s %-20s%n",
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getPhoneNumber(),
                    user.getAddress()));
        }
        return builder.toString();
    }

    public String fromUsersOrders(List<User> customers, Map<Long, List<ShopOrder>> userOrders) {
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("Customers and their orders:\n");
        reportBuilder.append("----------------------------------------------------------\n");

        for (User customer : customers) {
            reportBuilder.append("Customer: ").append(customer.getFirstName()).append(" ").append(customer.getLastName())
                    .append(" (ID: ").append(customer.getId()).append(")\n");

            List<ShopOrder> orders = userOrders.get(customer.getId());
            if (orders != null && !orders.isEmpty()) {
                for (ShopOrder order : orders) {
                    reportBuilder.append("  Order ID: ").append(order.getId())
                            .append(", Date: ").append(order.getOrderCreationDate())
                            .append(", Status: ").append(order.getStatus().getStatusName()).append("\n");
                }
            } else {
                reportBuilder.append("  No orders yet.\n");
            }
            reportBuilder.append("----------------------------------------------------------\n");
        }
        return reportBuilder.toString();
    }

    public String fromUserOrders(List<ShopOrder> orders, HttpSession session) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%-10s %-20s %-20s %-15s%n", "Order ID", "Order creation date", "Order status", "Total Amount"));
        builder.append("--------------------------------------------------------------------\n");

        Map<ShopOrder, Double> orderTotalAmounts = (Map<ShopOrder, Double>) session.getAttribute("orderTotalAmounts");

        for (ShopOrder order : orders) {
            Double totalAmount = orderTotalAmounts != null ? orderTotalAmounts.get(order) : null;

            builder.append(String.format("%-10d %-20s %-20s %-15s%n",
                    order.getId(),
                    order.getOrderCreationDate(),
                    order.getStatus().getStatusName(),
                    totalAmount != null ? String.format("%.2f", totalAmount) : "N/A"));
        }
        return builder.toString();
    }

}
