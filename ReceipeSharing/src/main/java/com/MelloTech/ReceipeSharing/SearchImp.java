package com.MelloTech.ReceipeSharing;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SearchImp implements SearchRepository {

    // Using the MongoDB client to communicate with the database
    @Autowired
    private MongoClient client;


    // Map MongoDB documents to Java objects using a converter
    @Autowired
    private MongoConverter converter;


    @Override
    public List<Post> findByText(String text) {
        final List<Post> posts = new ArrayList<>(); // declare list to store the search results

        // Open the'marshal' MongoDB database.
        MongoDatabase database = client.getDatabase("marshal");

        // Open the collection 'RecipePost' from the database
        MongoCollection<Document> collection = database.getCollection("RecipePost");

        //// Use an aggregation query to look for posts using text.
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
                new Document("$search", // MongoDB Atlas search operator
                        new Document("text", // Specifies a full-text search
                                new Document("query", text) // Text to search for
                                        .append("path", "name"))), // Field to search in
                new Document("$sort", // Sort the results
                        new Document("author_id", 1L)) // Sort by 'author_id' in ascending order
        ));

        //The aggregation result is iterated over, mapping documents to Post objects.
        result.forEach(doc -> posts.add(converter.read(Post.class, doc)));

        return posts; // return  a list of posts that meet the search parameters.
    }
}
