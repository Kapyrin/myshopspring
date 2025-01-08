package kapyrin.myshopspring.configuration;

import kapyrin.myshopspring.security.MyAuthenticationSuccessHandler;
import kapyrin.myshopspring.security.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("test")
@RequiredArgsConstructor
public class SecurityTestConfig {
    private final MyAuthenticationSuccessHandler successHandler;
    private final MyUserDetailsService userDetailsService;

    @Bean(name = "testPasswordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean(name = "testAuthenticationManager")
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean(name = "testSecurityFilterChain")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/language").permitAll()
                        .requestMatchers("/login").not().authenticated()
                        .requestMatchers("/createUser").permitAll()
                        .requestMatchers("/admin/", "/editUser", "/findUserById", "/deleteUser").hasRole("ADMIN")
                        .requestMatchers("/customerOrders/", "/createOrder/","order/createOrder").hasRole("CUSTOMER")
                        .requestMatchers("/editProduct/", "/managers/", "/addProduct", "/products").hasRole("MANAGER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .requestCache(request -> request.disable());


        return http.build();
    }
}
