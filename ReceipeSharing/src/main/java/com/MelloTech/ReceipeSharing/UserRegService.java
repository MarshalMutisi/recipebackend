package com.MelloTech.ReceipeSharing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service // Shows that this class is a component of the Spring service.
public class UserRegService {

    @Autowired
    private UserRepository userRepository; // Manages the Users entity's database operations

    @Autowired
    private JtService jtService; // Service for managing the creation of JWT tokens

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Password encryption utility

    // Method to register a new user
    public Users register(Users user) {
        // Before storing to the database, the user's password is encrypted.
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Returns the stored user object after saving the user in the database.
        return userRepository.save(user);
    }

    // method to confirm user information when logging in
    public String verify(Users user) {
        // get the user from the database by email
        Users existingUser = userRepository.findByEmail(user.getEmail());

        // verify  user exists and if the provided password matches the encrypted password
        if (existingUser != null && passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            // Generates and returns a JWT token for the authenticated user
            return jtService.generateToken(existingUser);
        }

        // Returns null if authentication fails
        return null;
    }
}
