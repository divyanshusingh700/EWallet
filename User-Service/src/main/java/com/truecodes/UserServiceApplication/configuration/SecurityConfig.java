package com.truecodes.UserServiceApplication.configuration;

import com.truecodes.UserServiceApplication.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Autowired
    private UserService userService;

    @Autowired
    private CommonConfig commonConfig;

    @Value("${user.Authority}")
    private String userAuthority;

    @Value("${admin.Authority}")
    private String adminAuthority;

    @Value("${service.Authority}")
    private String serviceAuthority;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(commonConfig.getEncoder());
        return authenticationProvider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(authorize -> authorize
////                                .requestMatchers("/user/userDetails/**").hasAnyAuthority(serviceAuthority, adminAuthority)
//                                .requestMatchers("/auth/**", "/user/login", "/user/verify-otp").permitAll()
////                                .requestMatchers("/user/filter/**").hasAnyAuthority(adminAuthority, studentAuthority)
////                        .requestMatchers("/txn/create/**").hasAuthority(adminAuthority)
////                        .requestMatchers("/txn/return/**").hasAuthority(adminAuthority)
////                        .requestMatchers("/book/addBook/**").hasAuthority(adminAuthority)
////                        .requestMatchers("/book/filter/**").hasAnyAuthority(studentAuthority, adminAuthority)
//                                .anyRequest().authenticated()
//
//                )
////                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt)
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
////                .formLogin(withDefaults()).httpBasic(withDefaults());
//
////        http.exceptionHandling()
////                .accessDeniedHandler((request, response, accessDeniedException) -> {
////                    System.out.println("Access denied: " + accessDeniedException.getMessage());
////                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
////                })
////                .authenticationEntryPoint((request, response, authException) -> {
////                    System.out.println("Authentication failed: " + authException.getMessage());
////                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
////                });
//
//        return http.build();
//    }

//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(); // You can configure strength here if needed
//    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}