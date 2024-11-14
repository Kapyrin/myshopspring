package kapyrin.myshopspring;

import kapyrin.myshopspring.entity.User;
import kapyrin.myshopspring.util.ReportStringGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class MyshopspringApplication {

    public static void main(String[] args) {
//        SpringApplication.run(MyshopspringApplication.class, args);
        ReportStringGenerator reportStringGenerator = new ReportStringGenerator();

        User user = User.builder()
                .firstName("First")
                .lastName("Lastt")
                .email("update@email.com")
                .password("password")
                .phoneNumber("6789012456")
                .address("Washington")
                .build();

        List<User> userList = List.of(user);

        String reportString = reportStringGenerator.fromUserList(userList);
        System.out.println(reportString);
    }

}
