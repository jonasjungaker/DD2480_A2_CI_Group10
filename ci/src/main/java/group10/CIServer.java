package group10;

import static spark.Spark.*;

public class CIServer {
    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        port(8080);
        // this is where HTML, CSS and images are stored
        staticFiles.location("/public");
        // this is the end point for displaying list of previous builds
        get("/", (req, res) -> "todo make a list of builds!");
        // this is the github post entrypoint
        post("/api/github", (req, res) -> "todo");

        exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });
    }
}

public static void post(String test, String state, String sha) {
    String user = "dieflo4711";
    String token = "secret";
    String repo = "test";
    String url = "https://api.github.com/repos/"+user+"/"+repo+"/statuses/" + sha;
    String json = "{'state': \'"+state+"\', 'description': 'desc', 'context': \'" + test +  "\', 'target_url': 'http://dleon.johvh.se'}"; 

    String command = "curl --request POST --data " + json + " -u " + user + ":" + token + " " + url;
    Process process = Runtime.getRuntime().exec(command);

}