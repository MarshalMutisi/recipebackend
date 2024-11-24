package com.MelloTech.ReceipeSharing;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@RestController
public class PostController {

// Injects the SearchRepository automatically to carry out search functions.
    @Autowired
    SearchRepository searchRepository;

    //// Post repository for post and get operations on posts
    private final PostRepository postRepository;

    // Constructor injection of the PostRepository
    @Autowired
    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @CrossOrigin(origins = "http://localhost:3000") // Allows cross-origin requests from the React frontend (localhost:3000)
    @GetMapping(value = "/posts") // fetch all posts at url /posts
    public List<Post> getAllPosts() {
        return postRepository.findAll(); // return all posts
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("posts/{text}") // return text from  "/posts/{text}"
    public List<Post> searchPosts(@PathVariable String text) {
        return searchRepository.findByText(text); // Searches posts based on the entered texts
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "/post") // navigate to "/post" for adding a new post
    public ResponseEntity<Post> addPost(@RequestBody Post post, HttpServletRequest request) {

        // getting authentication details from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // If the user is not authenticated, return a 401  Unauthorized response
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        // add the post to the database
        Post savedPost = postRepository.save(post);
        return ResponseEntity.ok(savedPost); //return saved  post with a 200 OK response for showing successfully
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("posts/id/{id}") // GET requests to "/posts/id/{id}" to fetch a specific post by its id
    public ResponseEntity<Post> getPostById(@PathVariable String id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            return ResponseEntity.ok(post.get()); // If the post exists, return it with a 200 OK response and the post
        }
        return ResponseEntity.notFound().build(); // If the post does not exist, return a 404 Not Found response
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/csrf-token") // GET requests to "/csrf-token" to retrieve the CSRF token
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf"); // Return the CSRF token from the request attributes
    }
}
