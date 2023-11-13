package org.jsoup.examples;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class ModifyingData {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("http://example.com/").get();
        doc.select("div.comments a").attr("rel", "nofollow");
    }

    public static void main2() throws IOException {
        Document doc = Jsoup.connect("http://example.com/").get();
        doc.select("div.masthead").attr("title", "jsoup").addClass("round-box");
    }

    public static void main3() throws IOException {
        Document doc = Jsoup.connect("http://example.com/").get();
        Element div = doc.select("div").first(); // <div></div>
        div.html("<p>lorem ipsum</p>"); // <div><p>lorem ipsum</p></div>
        div.prepend("<p>First</p>");
        div.append("<p>Last</p>");
        // now: <div><p>First</p><p>lorem ipsum</p><p>Last</p></div>

        Element span = doc.select("span").first(); // <span>One</span>
        span.wrap("<li><a href='http://example.com/'></a></li>");
        // now: <li><a href="http://example.com"><span>One</span></a></li>
    }

    public static void main4() throws IOException {
        Document doc = Jsoup.connect("http://example.com/").get();
        Element div = doc.select("div").first(); // <div></div>
        div.text("five > four"); // <div>five &gt; four</div>
        div.prepend("First ");
        div.append(" Last");
        // now: <div>First five &gt; four Last</div>
    }
}
