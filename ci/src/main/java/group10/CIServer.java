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