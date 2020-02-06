package group10;

import org.junit.Test;
//import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;


public class NotificationStatusTest
{
    String url = "https://api.github.com/repos/jonasjungaker/DD2480_A2_CI_Group10/statuses/{sha}";
    String sha = "";
    String inv_sha = "asdsadsa";

    /**
     * Tests for success by sending the status "success"
     */
    @Test
    public void testSuccessStatus()
    {
        assertTrue(GithubController.setCommitStatus(url, "success", sha));
    }

    /**
     * Tests for success by sending the status "failed"
     */
    @Test
    public void testFailStatus()
    {
        assertTrue(GithubController.setCommitStatus(url, "failed", sha));
        
    }

    /**
     * Tests for failure by using an invalid sha
     */
    @Test
    public void testInvalidSha()
    {
        assertFalse(GithubController.setCommitStatus(url, "failed", inv_sha));
    }
}