package kapyrin.myshopspring.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
      String password = "password";
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      System.out.println(encoder.encode(password));
    }
}

