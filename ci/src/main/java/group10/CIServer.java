package group10;

import static spark.Spark.*;

import group10.util.Path;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;

public class CIServer {
    static DatabaseHandler dbh = new DatabaseHandler();;

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        dbh.connect("ci");
        port(8080);
        // this is where HTML, CSS and images are stored
        staticFiles.location("/public");
        // this is the end point for displaying list of previous builds
        get(Path.HOME, (req, res) -> WebsiteController.handleGet(req, res));
        // this is the github post entrypoint
        post(Path.GITHUB, (req, res) -> GithubController.handlePost(req, res));

        // print if an error occurs
        exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });
    }

    /**
     * Parse a HttpServletRequest into JSONObject
     * @param request POST request
     * @return JSONObject parsed request
     * @throws IOException
     */
    public JSONObject parseRequest(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuffer buf = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            buf.append(line);
        }
        String data = buf.toString();
        if (data.length() == 0 || data.charAt(0) != '{') {
            return new JSONObject();
        } else  {
            return new JSONObject(data);
        }
    }
}