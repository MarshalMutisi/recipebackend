package com.MelloTech.ReceipeSharing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserRegService service; // Service layer for user registration and login logic

    @CrossOrigin(origins = "http://localhost:3000") // Allows requests from the React frontend
    @PostMapping("/register") // Directs HTTP POST requests to the endpoint /register.
    public ResponseEntity<?> register(@RequestBody Users user) {
        Users registeredUser = service.register(user); // Makes a call to the service to register the user

        registeredUser.setPassword(null); // For security purposes, the password is hidden in the response.
        return ResponseEntity.ok(registeredUser); // Provides the registered user and returns HTTP 200.
    }

    @CrossOrigin(origins = "http://localhost:3000")//Allows frontend requests to use CORS
    @PostMapping("/login") // Maps the /login endpoint to HTTP POST requests.
    public ResponseEntity<?> login(@RequestBody Users user) {
        String token = service.verify(user); // Creates a JWT token and confirms user credentials.

        if (token == null) { // Verifies whether the token generation was unsuccessful (invalid credentials).

            return ResponseEntity.status(401).body("Invalid credentials"); // Returns an Unauthorized HTTP 401
        }

        return ResponseEntity.ok(Map.of("token", token)); // Returns the generated token along with HTTP 200.
    }
}
