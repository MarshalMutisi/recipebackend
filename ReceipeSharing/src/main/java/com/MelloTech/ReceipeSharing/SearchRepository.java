package com.MelloTech.ReceipeSharing;

import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SearchRepository {

    // Method to search for posts based on a given text
    // The method returns a list of Post objects that match the search criteria
    List<Post> findByText(String Text);
}
