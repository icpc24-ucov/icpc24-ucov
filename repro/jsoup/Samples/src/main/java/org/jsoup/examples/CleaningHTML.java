package org.jsoup.examples;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class CleaningHTML {
    public static void main(String[] args) {
        String unsafe = "<p><a href='http://example.com/' onclick='stealCookies()'>Link</a></p>";
        String safe = Jsoup.clean(unsafe, Safelist.basic());
        // now: <p><a href="http://example.com/" rel="nofollow">Link</a></p>
    }
}
