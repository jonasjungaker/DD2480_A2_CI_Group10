package group10;

import spark.Request;
import spark.Response;

import org.json.JSONObject;

import group10.util.JsonUtil;

public class GithubController {
    private static JsonUtil json = new JsonUtil();

    /**
     * Handle the Github post event: get info, clone
     * run tests and return response.
     * @param request from github
     * @param response to send back
     * @return
     */
    public static String handlePost(Request request, Response response) {
        // turn data string into a map structure
        System.out.println(request.body());
        JSONObject all_data = json.toMap(request.body());   
        JSONObject relevant_data = json.getRelevantData(all_data);
        System.out.println(relevant_data.toString());
        
        // todo set commit pending
        // clone repo
        // compile repo
        // run tests
        // set commit
        return "todo";
    };
}