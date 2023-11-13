package org.example.GettingStarted;

import static spark.Spark.get;

public class HelloWorld {
    public static void main(String[] arg) {
        get("/hello", (request, response) -> "Hello World!");
    }
}