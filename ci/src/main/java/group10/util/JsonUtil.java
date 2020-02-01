package group10.util;

import org.json.JSONObject;

public class JsonUtil {
    public JSONObject toMap(String json) {
        if (json.length() == 0 || json.charAt(0) != '{') {
            return new JSONObject();
        }
        return new JSONObject(json);
    }

    public JSONObject getRelevantData(JSONObject all_data) {
        JSONObject relevant_data = new JSONObject();

        JSONObject repository = (JSONObject) all_data.get("repository");
        // following the format :owner/:repository
        relevant_data.put("full_name", repository.get("full_name"));
        // url used to ssh clone
        relevant_data.put("ssh_url", repository.get("ssh_url"));
        relevant_data.put("sha", all_data.get("after"));
        System.out.println("exiting");
        return relevant_data;
    }
}