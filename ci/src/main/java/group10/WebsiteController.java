package group10;

import spark.Request;
import spark.Response;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

public class WebsiteController {

    /**
     * Handle the Website get event: get info about build history from database
     * return response.
     * 
     * @param request  from website to server
     * @param response send back info from database
     * @return
     */
    public static String handleGet(Request request, Response response) {
        //JSONArray model = new JSONArray();
        // todo: get data from DB
        
        JSONArray builds = new JSONArray("[{\"date\":\"2020-02-05\",\"elapsed\":3.03,\"number_failed\":0,\"author\":\"pepe\",\"buildID\":70,\"number_passed\":0,\"branch\":\"cool\",\"status\":\"pending\"},{\"date\":\"2020-02-05\",\"elapsed\":0.02,\"number_failed\":0,\"author\":\"pepega\",\"buildID\":71,\"number_passed\":0,\"branch\":\"korv\",\"status\":\"passed\"}]");
        Map<String, Object> buildModel = new HashMap<>();
        buildModel.put("builds", builds);
        
        return new VelocityTemplateEngine().render(new ModelAndView(buildModel, "public/exampleTemplate.html"));
    } 
    
    public static String render(JSONArray model, String templatePath) {
        return new VelocityTemplateEngine().render(
            new ModelAndView(model, templatePath)
            );
    }

}