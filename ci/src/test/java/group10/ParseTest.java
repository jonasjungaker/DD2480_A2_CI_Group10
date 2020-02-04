package group10;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.json.*;
import org.mockito.*;

/**
 * Unit test for parse functionality.
 */
public class ParseTest {
    CIServer ci = new CIServer();

    /**
     * Parse example input
     */
    @Test
    public void parseTest() throws IOException {
        String hello = "{\"hello\":\"korv\"}";
        Reader inputString = new StringReader(hello);
        BufferedReader reader = new BufferedReader(inputString);
        
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getReader()).thenReturn(reader);
        JSONObject obj = ci.parseRequest(request);
        assertEquals(obj.toString(), hello);
    }

    /**
     * Parse empty string
     */
    @Test
    public void parseEmptyTest() throws IOException {
        String hello = "";
        Reader inputString = new StringReader(hello);
        BufferedReader reader = new BufferedReader(inputString);
        
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getReader()).thenReturn(reader);
        JSONObject obj = ci.parseRequest(request);
        assertEquals(obj.toString(), new JSONObject().toString());
    }
}
