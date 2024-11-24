package com.MelloTech.ReceipeSharing;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JtFilter extends OncePerRequestFilter {

    // JWT service autowiring to manage JWT-related tasks
    @Autowired
    private JtService jwtService;

    // loading user-specific data by automatically wiring the custom user details service
    @Autowired
    private MyUserDetailService myUserDetailService;

    // How to filter queries that come in
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Retrieve the request's Authorization header.
        String authHeader = request.getHeader("Authorization");

        // Set up the email and token variables.
        String token = null;
        String email = null;

        //Verify whether a bearer token is present in the authorization header.
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Remove the "Bearer" prefix from the token.
            token = authHeader.substring(7);
            // Take the token's email out.
            email = jwtService.extractUserName(token);
        }

        // If an email address is present and the context does not already have authentication set
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Use the retrieved email to load the user's information.
            Users user = (Users) myUserDetailService.loadUserByUsername(email);

            // Verify the token using the user's information.
            if (jwtService.validateToken(token, user)) {

                // Use the user's authority (roles/permissions) to create an authentication token.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                // Configure the authentication's details, which may contain session information or other details.
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Configure the SecurityContextHolder's authentication context.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Proceed with the filter chain, which forwards the request and response to the subsequent servlet or filter.
        filterChain.doFilter(request, response);
    }
}
