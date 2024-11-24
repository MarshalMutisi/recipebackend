package com.MelloTech.ReceipeSharing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    // Injecting the UserRepository
    @Autowired
    private UserRepository userRepository;

    // method to load user details by username (email)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // find the user from the database using the email
        Users user = userRepository.findByEmail(email);

        // If the user is not present, throw an exception
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Return the user object tht implements UserDetails
        return user;
    }
}
