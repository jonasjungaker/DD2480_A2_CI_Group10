package group10;

import spark.Request;
import spark.Response;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class WebsiteController {

    /**
     * Handle the Website get event: get info about build history from database
     * return response.
     * 
     * @param request  from website to server
     * @param response send back info from database
     * @return build history from database
     */
    public static String handleGet(Request request, Response response) {
        JSONArray builds = CIServer.dbh.getBuilds(0, 25);
        Map<String, Object> buildModel = new HashMap<>();
        buildModel.put("builds", builds);
        
        return new VelocityTemplateEngine().render(new ModelAndView(buildModel, "public/exampleTemplate.html"));
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
        // this breaks if we try to do /build/dnsjandkj for example.
        JSONObject build = CIServer.dbh.getBuild(Integer.parseInt(request.params(":buildid")));
        model.put("build", build);
                
        return new VelocityTemplateEngine().render(new ModelAndView(model, "public/build.html"));
    } 
    
    public static String render(JSONArray model, String templatePath) {
        return new VelocityTemplateEngine().render(
            new ModelAndView(model, templatePath)
            );
    }

}