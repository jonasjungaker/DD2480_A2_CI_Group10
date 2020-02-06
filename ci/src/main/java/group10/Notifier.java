package group10;

import okhttp3.*;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;

/**
 * An object for sending notifications to slack workspace, can be used through invoking the methods
 * notifier.sendNotification(Notifier.createMessage(buildSuccess, testSuccess, buildURL))
 */
class Notifier
{
    public String webHook;
    private static String slackWebhook = "https://hooks.slack.com/services/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";

    public Notifier()
    {
        this.webHook = slackWebhook;
    }

    /**
     * for sending notifications to slack
     * @param message a json string of data with contents that can be read by slack
     * @throws IOException if the message was not sent
     */
    public void sendNotification(String message) throws IOException
    {
        // Json builder
        // JSONObject json = new JSONObject();
        // json.put("text", message);
        this.post(message);
    }

    /**
     * For posting to slack, content is the content of the message to send to slack
     * @param content string with json data
     * @throws IOException if the messsage was not sent
     */
    private void post(String content) throws IOException
    {
        // Set up a client for sending notifications
        OkHttpClient client = new OkHttpClient();
        // Building the post body and request
        RequestBody body = RequestBody.create(content, MediaType.get("application/json; charset=utf-8"));
        Request req = new Request.Builder().url(this.webHook).post(body).build();
        // Handling the response
        try (Response res = client.newCall(req).execute();){
            System.out.println(res.body().string());
        }
    }

    /**
     * Method for creating a json payload for interpreting through slack
     * @param buildSuccess whether the build was successful
     * @param testSuccess whether the tests were successful
     * @param buildURL the link for the build in question
     * @return the string of the json object that can be interpreted by slack
     */
    public static String createMessage(boolean buildSuccess, boolean testSuccess, String buildURL)
    {
        String header;
        if (buildSuccess && testSuccess){
            header = "New build success";
        }
        else {
            header = "Build failure at repository";
        }

        // Create the main outline of the message
        JSONObject json = new JSONObject();
        JSONArray blocks = new JSONArray();

        // Section 1 of the message
        JSONObject section1 = new JSONObject();
        section1.put("type", "section");
        JSONObject jo = new JSONObject();
        jo.put("type", "mrkdwn");
        jo.put("text", header);
        section1.put("text", jo);
        blocks.put(section1);

        // Section 2 of the message
        JSONObject section2 = new JSONObject();
        section2.put("type", "section");
        JSONObject jo2 = new JSONObject();
        jo2.put("type", "mrkdwn");
        jo2.put("text", String.format("See build <%s|here>", buildURL));
        section2.put("text", jo2);
        blocks.put(section2);

        // Return the message
        json.put("blocks", blocks);
        return json.toString();
    }
}