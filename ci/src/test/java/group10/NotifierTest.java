package group10;

import org.junit.Test;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;


public class NotifierTest
{
    @Test
    public void messageUrlTest()
    {
        // test that the message contains the link to the url provided
        String message = Notifier.createMessage(true, true, "http://someurl.home/");
        assertTrue(message.contains("http://someurl.home/"));
    }

    @Test
    public void messageSuccessfulTest()
    {
        String message = Notifier.createMessage(true, true, "123");
        assertTrue(message.contains("success"));
    } 

    @Test
    public void messageFailureTest()
    {
        String message = Notifier.createMessage(true, false, "123");
        assertTrue(message.contains("failure"));
    } 
}