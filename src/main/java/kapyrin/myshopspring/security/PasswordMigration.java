package kapyrin.myshopspring.security;

import kapyrin.myshopspring.entity.User;
import kapyrin.myshopspring.service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@Component
public class PasswordMigration implements CommandLineRunner {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting password migration");
        List<User> checkingUsers = userService.getAll();
        if (checkingUsers.isEmpty()) {
            log.info("No users found for password migration");
            return;
        }
        for (User user : checkingUsers) {
            if (!user.getPassword().startsWith("$2")) {
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
                userService.update(user);
                log.info("Password updated for user: {}", user.getEmail());
            }
        }
        log.info("Passwords of users migration has been completed");
    }

}
