package spark.template.freemarker;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import spark.ModelAndView;

import static org.junit.Assert.assertEquals;

public class FreeMarkerEngineTest {

    @Test
    public void test_render() throws Exception {
        String templateVariable = "Hello Freemarker!";
        Map<String, Object> model = new HashMap<>();
        model.put("message", templateVariable);
        String expected = String.format("<h1>%s</h1>", templateVariable);
        String actual = new FreeMarkerEngine().render(new ModelAndView(model, "hello.ftl"));
        assertEquals(expected, actual);
    }

}