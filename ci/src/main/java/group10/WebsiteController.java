package group10;

import spark.Request;
import spark.Response;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import java.util.HashMap;
import java.util.Map;

public class WebsiteController {

    /**
     * Handle the Website get event: get info about build history from database
     * return response.
     * @param request from website to server
     * @param response send back info from database
     * @return build history from database
     */
    public static String handleGet(Request request, Response response) 
    {
        Map<String, Object> model = new HashMap<>();
        //todo: get data from DB
        
        return new VelocityTemplateEngine().render(new ModelAndView(model, "public/remakeToTemplate.html"));
    } 

    /**
     * Handle the Website get event: get info on specific build
     * @param request from website to server
     * @param response send back info from database
     * @return VelocityTemplateEngine with the hmtl page for a specific build
     */
    public static String handleGetBuild(Request request, Response response) 
    {
        Map<String, Object> model = new HashMap<>();
        model.put("buildId", request.params(":buildid"));
                
        return new VelocityTemplateEngine().render(new ModelAndView(model, "public/buildPage.html"));
    } 
    
    public static String render(Map<String, Object> model, String templatePath) {
        return new VelocityTemplateEngine().render(
            new ModelAndView(model, templatePath)
            );
    }

}