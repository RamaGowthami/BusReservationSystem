package com.example.reservation.config;



import com.example.reservation.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return username -> {
            com.example.reservation.model.User user = userService.findByUsername(username);
            if (user == null) throw new RuntimeException("User not found");

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole().replace("ROLE_", ""))
                    .build();
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/register","/login", "/css/**", "/swagger-ui.html","/swagger-ui/**", "/v3/api-docs/**","/v3/api-docs.yaml").permitAll()
                        //.requestMatchers("/appointments/bookForm").authenticated()
                        .requestMatchers("/booking/options").authenticated()
                        .requestMatchers("/bus/register-passengers/**").authenticated() // Add this line
                        .anyRequest().authenticated()
                )
                .formLogin().defaultSuccessUrl("/home", true).permitAll()
                .and().logout().permitAll();
        return http.build();
    }


}
