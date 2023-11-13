package org.example2.GettingStarted;

import com.google.gson.Gson;
import spark.ModelAndView;
import spark.Redirect;
import spark.template.velocity.VelocityTemplateEngine;

import javax.servlet.MultipartConfigElement;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class HelloWorld {
    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
        initExceptionHandler((e) -> System.out.println("Uh-oh"));

        before((request, response) -> {
            boolean authenticated;
            // ... check if authenticated
            halt(401, "You are not welcome here");
        });
        after((request, response) -> {
            response.header("foo", "set by after filter");
        });
        afterAfter((request, response) -> {
            response.header("foo", "set by afterAfter filter");
        });
        before("/protected/*", (request, response) -> {
            // ... check if authenticated
            halt(401, "Go Away!");
        });
        get("/", (request, response) -> {
            halt();                // halt
            halt(401);             // halt with status
            halt("Body Message");  // halt with message
            halt(401, "Go away!"); // halt with status and message
            // Show something
            return "";
        });

        post("/", (request, response) -> {
            // Create something

            request.cookies();                         // get map of all request cookies
            request.cookie("foo");                     // access request cookie by name
            response.cookie("foo", "bar");             // set cookie with a value
            response.cookie("foo", "bar", 3600);       // set cookie with a max-age
            response.cookie("foo", "bar", 3600, true); // secure cookie
            response.removeCookie("foo");              // remove cookie

            return "";
        });

        put("/", (request, response) -> {
            // Update something

            request.queryMap().get("user", "name").value();
            request.queryMap().get("user").get("name").value();
            request.queryMap("user").get("age").integerValue();
            request.queryMap("user").toMap();
            return "";
        });

        delete("/", (request, response) -> {
            // Annihilate something

            response.body();               // get response content
            response.body("Hello");        // sets content to Hello
            response.header("FOO", "bar"); // sets header FOO with value bar
            response.raw();                // raw response handed in by Jetty
            response.redirect("/example"); // browser redirect to /example
            response.status();             // get the response status
            response.status(401);          // set status code to 401
            response.type();               // get the content type
            response.type("text/xml");     // set content type to text/xml

            return "";
        });

        options("/", (request, response) -> {
            // Appease something
            request.attributes();             // the attributes list
            request.attribute("foo");         // value of foo attribute
            request.attribute("A", "V");      // sets value of attribute A to V
            request.body();                   // request body sent by the client
            request.bodyAsBytes();            // request body as bytes
            request.contentLength();          // length of request body
            request.contentType();            // content type of request.body
            request.contextPath();            // the context path, e.g. "/hello"
            request.cookies();                // request cookies sent by the client
            request.headers();                // the HTTP header list
            request.headers("BAR");           // value of BAR header
            request.host();                   // the host, e.g. "example.com"
            request.ip();                     // client IP address
            request.params("foo");            // value of foo path parameter
            request.params();                 // map with all parameters
            request.pathInfo();               // the path info
            request.port();                   // the server port
            request.protocol();               // the protocol, e.g. HTTP/1.1
            request.queryMap();               // the query map
            request.queryMap("foo");          // query map for a certain parameter
            request.queryParams();            // the query param list
            request.queryParams("FOO");       // value of FOO query param
            request.queryParamsValues("FOO"); // all values of FOO query param
            request.raw();                    // raw request handed in by Jetty
            request.requestMethod();          // The HTTP method (GET, ..etc)
            request.scheme();                 // "http"
            request.servletPath();            // the servlet path, e.g. /result.jsp
            request.session();                // session management
            request.splat();                  // splat (*) parameters
            request.uri();                    // the uri, e.g. "http://example.com/foo"
            request.url();                    // the url. e.g. "http://example.com/foo"
            request.userAgent();              // user agent
            return "";
        });

// matches "GET /hello/foo" and "GET /hello/bar"
// request.params(":name") is 'foo' or 'bar'
        get("/hello/:name", (request, response) -> {
            response.redirect("/bar");
            response.redirect("/bar", 301); // moved permanently
            return "Hello: " + request.params(":name");
        });

        // matches "GET /say/hello/to/world"
// request.splat()[0] is 'hello' and request.splat()[1] 'world'
        get("/say/*/to/*", (request, response) -> {
            // redirect a GET to "/fromPath" to "/toPath"
            redirect.get("/fromPath", "/toPath");

// redirect a POST to "/fromPath" to "/toPath", with status 303
            redirect.post("/fromPath", "/toPath", Redirect.Status.SEE_OTHER);

// redirect any request to "/fromPath" to "/toPath" with status 301
            redirect.any("/fromPath", "/toPath", Redirect.Status.MOVED_PERMANENTLY);
            return "Number of splat parameters: " + request.splat().length;
        });

        unmap("/hello"); // unmaps all routes with path 'hello'
        unmap("/hello", "get"); // unmaps all 'GET' routes with path 'hello'

        path("/api", () -> {
            before("/*", (q, a) -> log.info("Received api call"));
            path("/email", () -> {
                post("/add", EmailApi.addEmail);
                put("/change", EmailApi.changeEmail);
                delete("/remove", EmailApi.deleteEmail);
            });
            path("/username", () -> {
                post("/add", UserApi.addUsername);
                put("/change", UserApi.changeUsername);
                delete("/remove", UserApi.deleteUsername);
            });
        });
// Using string/html
        notFound("<html><body><h1>Custom 404 handling</h1></body></html>");
        // Using Route
        notFound((req, res) -> {
            res.type("application/json");
            return "{\"message\":\"Custom 404\"}";
        });
        // Using string/html
        internalServerError("<html><body><h1>Custom 500 handling</h1></body></html>");
        // Using Route
        internalServerError((req, res) -> {
            res.type("application/json");
            return "{\"message\":\"Custom 500 handling\"}";
        });
        get("/throwexception", (request, response) -> {
            throw new YourCustomException();
        });

        exception(YourCustomException.class, (exception, request, response) -> {
            // Handle the exception here
        });
        // root is 'src/main/resources', so put files in 'src/main/resources/public'
        staticFiles.location("/public"); // Static files
        staticFiles.externalLocation(System.getProperty("java.io.tmpdir"));
        staticFiles.expireTime(600); // ten minutes
        staticFiles.header("Key-1", "Value-1");
        staticFiles.header("Key-1", "New-Value-1"); // Using the same key will overwrite value
        staticFiles.header("Key-2", "Value-2");
        staticFiles.header("Key-3", "Value-3");
        get("/hello", "application/json", (request, response) -> {
            return new MyMessage("Hello World");
        }, new JsonTransformer());
        Gson gson = new Gson();
        get("/hello", (request, response) -> new MyMessage("Hello World"), gson::toJson);
        // do this
        get("template-example", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new VelocityTemplateEngine().render(new ModelAndView(model, "path-to-template"));
        });
        // don't do this
        get("template-example", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "path-to-template");
        }, new VelocityTemplateEngine());

        get("template-example", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "path-to-template");
        });
        port(8080); // Spark will run on port 8080
        String keystoreFilePath = "";
        String keystorePassword = "";
        String truststoreFilePath = "";
        String truststorePassword = "";
        secure(keystoreFilePath, keystorePassword, truststoreFilePath, truststorePassword);
        int maxThreads = 8;
        threadPool(maxThreads);
        int minThreads = 2;
        int timeOutMillis = 30000;
        threadPool(maxThreads, minThreads, timeOutMillis);
        awaitInitialization(); // Wait for server to be initialized
        webSocket("/echo", EchoWebSocket.class);
        init(); // Needed if you don't define any HTTP routes after your WebSocket routes
        get("/some-path", (request, response) -> {
            // code for your get
            response.header("Content-Encoding", "gzip");
            return "";
        });
        after((request, response) -> {
            response.header("Content-Encoding", "gzip");
        });
        post("/yourUploadPath", (request, response) -> {
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            try (InputStream is = request.raw().getPart("uploaded_file").getInputStream()) {
                // Use the input stream to create a file
            }
            return "File uploaded";
        });
        String keyStoreLocation = "deploy/keystore.jks";
        String keyStorePassword = "password";
        secure(keyStoreLocation, keyStorePassword, null, null);
        String projectDir = System.getProperty("user.dir");
        String staticDir = "/src/main/resources/public";
        staticFiles.externalLocation(projectDir + staticDir);
        staticFiles.location("/public");
        stop();
    }

    // declare this in a util-class
    public static String render(Map<String, Object> model, String templatePath) {
        return new VelocityTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}