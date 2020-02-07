package group10;

import org.junit.Test;
import group10.GithubController;
import org.json.JSONObject;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;


public class NotificationStatusTest
{
    String url = "https://api.github.com/repos/jonasjungaker/DD2480_A2_CI_Group10/statuses/{sha}";
    String sha = "7c1e7acfac4e57de1a10a1bf1065c5219974a59c";
    String inv_sha = "asdsadsa";
    JSONObject relevant_data = new JSONObject();
    

    /**
     * Tests for success by sending the status "success"
     */
    @Test
    public void testSuccessStatus()
    {   
        relevant_data.put("statuses_url", url);
        relevant_data.put("sha", sha);
        assertTrue(GithubController.setCommitStatus(relevant_data, null, "success", 1));
    }

    /**
     * Tests for success by sending the status "failed"
     */
    @Test
    public void testFailStatus()
    {
        relevant_data.put("statuses_url", url);
        relevant_data.put("sha", sha);
        assertTrue(GithubController.setCommitStatus(relevant_data, null, "builedFailed", 1));
        
    }

    /**
     * Tests for failure by using an invalid sha
     */
    @Test
    public void testInvalidSha()
    {
        relevant_data.put("statuses_url", url);
        relevant_data.put("sha", inv_sha);
        assertFalse(GithubController.setCommitStatus(relevant_data, null, "success", 1));
    }
}