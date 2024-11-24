package com.MelloTech.ReceipeSharing;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

// Repository interface for MongoDB 'Post' records that allows CRUD operations

public interface PostRepository extends MongoRepository<Post, String> {

    //method to find a post by its id

    Optional<Post> findById(String id);
}
