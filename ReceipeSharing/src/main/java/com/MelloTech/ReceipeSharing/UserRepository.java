package com.MelloTech.ReceipeSharing;

import com.MelloTech.ReceipeSharing.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

// Repository interface for performing database operations on the Users collection
public interface UserRepository extends MongoRepository<Users, String> {

    // method to find a user by their email address
    Users findByEmail(String email);

}
