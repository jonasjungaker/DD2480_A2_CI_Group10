package group10;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONObject;
import org.junit.Test;

/**
 * Test for reading result from tests into json object
 */
public class ReadTestResultTest {
    /**
     * Test with:
     * One test that fails with AssertionError
     * Two passing tests
     * 
     * @throws ParserConfigurationException
     */
    @Test
    public void resultToJsonTest() throws ParserConfigurationException
    {
        ReadTestResults rtr = new ReadTestResults();
        JSONObject json = rtr.read("/", "test-results");
        assertEquals(1, json.getInt("number_failed"));
        assertEquals(1, json.getInt("number_success"));
        JSONObject n = (JSONObject) json.getJSONArray("failed").get(0);
        String cause = n.getString("cause");
        assertTrue(cause.length() <= 200);
        assertTrue(cause.contains("AssertionError"));
        assertTrue(n.getString("time").contains("0.005"));
        assertTrue(n.getString("classname").contains("AppTest"));
    }

    /**
     * Test with:
     * All passing tests, ensure that no fails exist and that names for
     * succeded are correct
     * @throws ParserConfigurationException
     */
    @Test
    public void resultToJsonPassingTest() throws ParserConfigurationException
    {
        ReadTestResults rtr = new ReadTestResults();
        JSONObject json = rtr.read("/", "test-results-pass");
        assertEquals(0, json.getInt("number_failed"));
        assertEquals(1, json.getInt("number_success"));
	
        JSONObject n2 = (JSONObject) json.getJSONArray("succeded").get(0);
        assertTrue(n2.getString("name").contains("shouldAnswerWithTrue"));
        assertTrue(n2.getString("classname").contains("AppTest"));
        assertTrue(n2.getString("time").contains("0.005"));
    }
}
