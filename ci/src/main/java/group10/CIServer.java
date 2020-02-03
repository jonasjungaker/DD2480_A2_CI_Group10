package group10;

import static spark.Spark.*;

import group10.util.Path;

public class CIServer {
    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        port(8080);
        // this is where HTML, CSS and images are stored
        staticFiles.location("/public");
        // this is the end point for displaying list of previous builds
        get(Path.HOME, (req, res) -> "todo make a list of builds!");
        // this is the github post entrypoint
        post(Path.GITHUB, (req, res) -> GithubController.handlePost(req, res));

        // print if an error occurs
        exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });
    }
}