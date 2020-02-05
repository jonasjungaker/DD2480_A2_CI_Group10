package group10;

import spark.Request;
import spark.Response;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import group10.util.JsonUtil;

public class GithubController {
    private static JsonUtil json = new JsonUtil();
    private static CIServer ciServer = new CIServer();

    /**
     * Handle the Github post event: get info, clone run tests and return response.
     * 
     * @param request  from github
     * @param response to send back
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static String handlePost(Request request, Response response){
        // turn data string into a map structure
        System.out.println(request.body());
        JSONObject all_data = json.toMap(request.body());   
        JSONObject relevant_data = json.getRelevantData(all_data);
        System.out.println(relevant_data.toString());
        
        // todo set commit pending
        // clone repo
        File cloneDirectoryPath = new File("clone");
        boolean cloned = false;
        try {
            cloned = ciServer.cloneRepository(relevant_data.getString("clone_url"), relevant_data.getString("ref"),
                    cloneDirectoryPath);
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Failed cloning with: "+ e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed cloning with: "+ e.getMessage());
        }
        
        // compile repo
        // run tests
        // set commit

        //tear down the session
        try {
            ciServer.tearDown(cloneDirectoryPath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed tearDown "+ e.getMessage());
        }

        //Check that everything went as it should
        if(cloned){
            return "success";
        }
        return "failed";
    };
}