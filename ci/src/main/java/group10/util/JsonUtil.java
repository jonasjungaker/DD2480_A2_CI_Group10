package group10.util;

import org.json.JSONObject;

public class JsonUtil {
    
    /** 
     * Turns String json to a JSONObject.
     * @param json String
     * @return JSONObject
     */
    public JSONObject toMap(String json) {
        if (json.length() == 0 || json.charAt(0) != '{') {
            return new JSONObject();
        }
        return new JSONObject(json);
    }
    
    
    /** 
     * Get the data needed to clone and update
     * commit message.
     * @param all_data all data from Github post request
     * @return JSONObject relevant data
     */
    public JSONObject getRelevantData(JSONObject all_data) {
        JSONObject relevant_data = new JSONObject();

        JSONObject repository = (JSONObject) all_data.get("repository");
        JSONObject pusher = (JSONObject) all_data.get("pusher");
        // following the format :owner/:repository
        relevant_data.put("full_name", repository.get("full_name"));
        // url used to ssh clone
        relevant_data.put("ssh_url", repository.get("ssh_url"));
        relevant_data.put("sha", all_data.get("after"));
        relevant_data.put("ref", all_data.get("ref"));
        relevant_data.put("clone_url", repository.get("clone_url"));
        relevant_data.put("author", pusher.get("name"));
        relevant_data.put("statuses_url", repository.get("statuses_url"));
        return relevant_data;
    }
}