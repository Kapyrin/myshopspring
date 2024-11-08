package kapyrin.myshopspring.controller.util;

import kapyrin.myshopspring.entity.ShopOrder;
import kapyrin.myshopspring.entity.User;
import kapyrin.myshopspring.util.ReportStringGenerator;
import kapyrin.myshopspring.util.ReportWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.util.List;
import java.util.Map;

@Controller
public class ReportController {
    private final ReportWriter reportWriter;
    private final ReportStringGenerator reportStringGenerator;

    @Autowired
    public ReportController(ReportWriter reportWriter, ReportStringGenerator reportStringGenerator) {
        this.reportWriter = reportWriter;
        this.reportStringGenerator = reportStringGenerator;
    }

    @GetMapping("/report")
    public ResponseEntity<Resource> generateReport(@RequestParam("reportType") String reportType, HttpSession session) {


        String reportContent;
        String fileName;

        switch (reportType) {
            case "usersOrders":
                List<User> onlyCustomers = (List<User>) session.getAttribute("users");
                Map<Long, List<ShopOrder>> userOrders = (Map<Long, List<ShopOrder>>) session.getAttribute("userOrders");

                if (onlyCustomers == null || userOrders == null) {
                    return ResponseEntity.badRequest().build();
                }

                reportContent = reportStringGenerator.fromUsersOrders(onlyCustomers, userOrders);
                fileName = "userOrdersReport.txt";
                break;

            case "allUsers":
                List<User> allUsers = (List<User>) session.getAttribute("users");

                if (allUsers == null) {
                    return ResponseEntity.badRequest().build();
                }

                reportContent = reportStringGenerator.fromUserList(allUsers);
                fileName = "allUsersReport.txt";
                break;

            case "userOrders":
                List<ShopOrder> personalOrders = (List<ShopOrder>) session.getAttribute("orders");

                if (personalOrders == null || personalOrders.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }

                reportContent = reportStringGenerator.fromUserOrders(personalOrders, session);
                fileName = "userPersonalOrdersReport.txt";
                break;

            default:
                return ResponseEntity.badRequest().build();
        }

        File reportFile = reportWriter.saveStringToFile(reportContent, fileName);

        Resource resource = new FileSystemResource(reportFile);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + reportFile.getName() + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }
}
