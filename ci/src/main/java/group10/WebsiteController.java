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
        JSONArray builds = CIServer.dbh.getBuilds(0, 25);
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