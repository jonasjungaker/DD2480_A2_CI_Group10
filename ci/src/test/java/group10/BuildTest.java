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
        String buildOutput = "building...\nBUILD SUCCESSFUL\nbuilding...\n";

        BuildData bd = CloneBuilder.checkBuildOutput(buildOutput);
        assertTrue(bd.buildSuccess);
        assertTrue(bd.testSuccess);
    }

    /**
     * Testing that the builder recognizes a fault in the testing
     */
    @Test
    public void testFailTest()
    {
        String message = "[ERROR] Failures:\nSome Message with data containing\ninformation about how the test failed\n";
        String buildOutput = "building...\n" + message + "[INFO] BUILD FAILURE\nbuilding...\n";

        BuildData bd = CloneBuilder.checkBuildOutput(buildOutput);
        assertTrue(bd.buildSuccess);
        assertFalse(bd.testSuccess);
    }

    /**
     * Testing that the builder recognizes a fault in compiling
     */
    @Test
    public void compileFailTest()
    {
        String message = "[ERROR] COMPILATION ERROR : \nSome Message with data containing\ninformation about how the test failed\n";
        String buildOutput = "building...\n" + message + "[INFO] BUILD FAILURE\nbuilding...\n";

        BuildData bd = CloneBuilder.checkBuildOutput(buildOutput);
        assertFalse(bd.buildSuccess);
        assertFalse(bd.testSuccess);
    }
}