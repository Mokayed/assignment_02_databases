/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenmangodb;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.util.Arrays;
import org.bson.Document;

public class MainDb {

    public static void main(String[] args) {
        //dbContest();
        userCount();
        mostActive();
        mentionsUser();
    }

    static MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    static Block<Document> printBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            System.out.println(document.toJson());
        }
    };
// this methode is made only for testing...
    public static void dbConTest() {
        MongoDatabase database = mongoClient.getDatabase("tweets");
        MongoCollection<Document> coll = database.getCollection("things");
        coll.find(eq("user", "2Hood4Hollywood"))
                .forEach(printBlock);
    }

    public static void userCount() {
        MongoDatabase database = mongoClient.getDatabase("tweets");
        MongoCollection<Document> coll = database.getCollection("things");
        long value = coll.count();
        System.out.println(value);
    }

    public static void mostActive() {
        MongoDatabase database = mongoClient.getDatabase("tweets");
        MongoCollection<Document> coll = database.getCollection("things");
        AggregateIterable<Document> output = coll.aggregate(Arrays.asList(
                new Document("$unwind", "$user"),
                new Document("$group", "{ _id: \"$user\", number: { $sum: 1 } }"),
                new Document("$sort", "{ number: -1 }"),
                new Document("$limit", 10)
        ));
        for (Document dbObject : output) {
            System.out.println(dbObject);
        }

    }

    public static void mentionsUser() {
        MongoDatabase database = mongoClient.getDatabase("tweets");
        MongoCollection<Document> coll = database.getCollection("things");
        AggregateIterable<Document> output = coll.aggregate(Arrays.asList(
                new Document(" $match", "{ text: /@\\w*/ }"),
                new Document("$unwind", "{$user}"),
                new Document("$group:", "_id: \"$user\", number: { $sum: 1 }"),
                new Document("$sort", "{ number: -1 }"),
                new Document("$limit", 10)
        ));
        for (Document dbObject : output) {
            System.out.println(dbObject);
        }

    }
}
