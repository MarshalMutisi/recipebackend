package com.MelloTech.ReceipeSharing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JtFilter jtFilter; // Using the JWT filter to manage token-based authentication

    @Autowired
    private MyUserDetailService myUserDetailService; //Custom user detail service for user data loading


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // Disabling CSRF protection (cross-site request forgery)
                .authorizeRequests() // start authorization configuration
                .requestMatchers("/register", "/login", "/posts", "/posts/id").permitAll()// Endpoints that are open to the public
                .requestMatchers("/post").authenticated() // Only authorized users are allowed access.
                .and()
                .addFilterBefore(jtFilter, UsernamePasswordAuthenticationFilter.class) //JWT filter is added before to username-password authentication.
                .cors(); // Enables Cross-Origin Resource Sharing (CORS)

        return http.build(); // Builds and returns the security filter chain
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Provides a password encoder using BCrypt hashing algorithm
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return myUserDetailService; // return the unique user detail service bean.
    }
}
