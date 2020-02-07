package group10;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;


/**
 * Testing the build/compilation mechanism
 */
public class BuildTest 
{

    /**
     * Testing that the builder recognizes a success
     */
    @Test
    public void successfulTest()
    {
        CloneBuilder builder = new CloneBuilder("");
        builder.buildOutput = "building...\nBUILD SUCCESSFUL\nbuilding...Failures: 0\n";

        boolean b = builder.checkBuildOutput();
        assertTrue(b);
    }

    /**
     * Testing that the builder recognizes a fault in the testing
     */
    @Test
    public void testFailTest()
    {
        String message = "[ERROR] Failures:\nSome Message with data containing\ninformation about how the test failed\n";
        CloneBuilder builder = new CloneBuilder("");
        builder.buildOutput = "building...\n" + message + "[INFO] BUILD FAILURE\nbuilding...\n";

        assertFalse(builder.checkBuildOutput());
    }
}