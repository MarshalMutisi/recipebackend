package com.MelloTech.ReceipeSharing;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Class to configure CORS
public class WebConfigure implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply CORS
                .allowedOrigins("http://localhost:3000") // Allow requests from http://localhost:3000"
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Specify HTTP methods allowed for cross-origin requests
                .allowedHeaders("Content-Type", "Authorization") // Allow these headers in cross-origin requests
                .allowCredentials(true); // Allow cookies and credentials in cross-origin requests
    }
}
