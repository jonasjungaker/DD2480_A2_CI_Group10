package group10;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.*;

/** 
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.
*/
public class CIServer extends AbstractHandler
{
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

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        JSONObject body = parseRequest(request);

        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        // 2nd compile the code

        response.getWriter().println("CI job done");
    }
 
    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new CIServer()); 
        server.start();
        server.join();
    }
}