package group10;

import spark.Request;
import spark.Response;

import org.json.JSONObject;

import group10.util.JsonUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Base64;
import java.net.URL;

public class GithubController {
    private static JsonUtil json = new JsonUtil();

    /**
     * Handle the Github post event: get info, clone run tests and return response.
     * 
     * @param request  from github
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
        post("test1", "success", relevant_data.get("sha").toString());
        post("test2", "failure", relevant_data.get("sha").toString());

        return "todo";
    };

    public static void post(String test, String state, String sha) {
        String user = "dieflo4711";
        String token = "password";
        String repo = "test";
        String url = "https://api.github.com/repos/" + user + "/" + repo + "/statuses/" + sha;
        String json = "{\"state\": \"" + state + "\", \"description\": \"desc\", \"context\": \"" + test
                + "\", \"target_url\": \"http://dleon.johvh.se\"}";
        
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty ("Authorization", "Basic " + Base64.getEncoder().encodeToString((user+":"+token).getBytes()));
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(json);
            wr.flush();
            wr.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            in.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
