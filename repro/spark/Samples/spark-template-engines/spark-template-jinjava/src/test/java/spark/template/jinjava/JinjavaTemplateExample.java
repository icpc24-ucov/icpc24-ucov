package spark.template.jinjava;

import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;

import static spark.Spark.get;

public class JinjavaTemplateExample {

    public static void main(String[] args) {

        get("/hello", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("message", "Hello Jinjava!");
            return new ModelAndView(model, "template/hello.jin");
        }, new JinjavaEngine());

    }

}
