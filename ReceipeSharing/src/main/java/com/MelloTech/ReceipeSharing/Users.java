package com.MelloTech.ReceipeSharing;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Document(collection = "User")// Indicates that the "User" collection contains a MongoDB document for this class.
public class Users implements UserDetails { // implements UserDetails into practice to integrate with Spring Security

    private String email; // The email address of the user acts as a unique identifier (username).
    private String password; // Encrypted password for authentication
    private String role; // User's role for authorization (e.g., ADMIN, USER)

    // Getter and setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and setter for role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Lists the user's permissions according to their position.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>(); // List to store authorities

        // Adds one authority based on the role of the user.
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.role));
        return authorities;
    }

    // Provides the user's email address as the authentication username.
    @Override
    public String getUsername() {
        return this.email;
    }

    // Indicates whether the user's account is expired (true means not expired)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Indicates whether the user's account is locked (true means not locked)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Indicates whether the user's credentials are expired (true means not expired)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Indicates whether the user's account is enabled (true means enabled)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
