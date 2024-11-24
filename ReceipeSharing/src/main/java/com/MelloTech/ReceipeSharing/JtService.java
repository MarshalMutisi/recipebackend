package com.MelloTech.ReceipeSharing;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JtService {

    // The JWT token is signed using a secret key
    private String secretKey;

    // Constructor to use the HMACSHA256 algorithm to generate a secret key
    public JtService() {
        try {
            // Create a secret key for the HMACSHA256 cryptosystem.
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey(); // Generate the key
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating secret key", e);
        }
    }

    // GenerateToken method for a given user
    public String generateToken(Users user) {
        return Jwts.builder()
                .setSubject(user.getEmail()) // Email set as  subject of the token
                .setIssuedAt(new Date()) // issued timestamp
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Set expiration to 1 day from current time
                .signWith(getKey()) // using the secret key to sign
                .compact(); // Return  condensed JWT string

    }

    // How to retrieve a JWT token's username (email)
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject); // retrieve the subject  from the token
    }

    // Generic method to retrieve a specific claim  from a JWT token
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token); // retrieve all claims from the token
        return claimResolver.apply(claims); // Use the assertionfunction for resolution (such as Claims::getSubject)

    }

    // how to retrieve  all claims from the JWT token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())// Configure the signing key to confirm the token.
                .build()
                .parseClaimsJws(token)// Token parsing for JWT
                .getBody(); // Return the claims body
    }

    // method  verify the token by making sure the username and the token are the same
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token); // Extract  username(email)
        // Verify that the token has not expired and that the username matches.
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Method to verify if  token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // verify if the  date has passed
    }

    // Method to show the expiration date
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // return date from claims
    }

   //// method to extract the Base64-encoded string's secret key and turn it into a SecretKey
    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Decode the secret key from Base64 string
        return Keys.hmacShaKeyFor(keyBytes); // return  the secret key from the Base64-encoded string and turn it into a SecretKey

    }
}
