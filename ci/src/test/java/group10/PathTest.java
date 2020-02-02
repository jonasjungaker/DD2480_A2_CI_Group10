package group10;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;

import org.junit.Test;

import group10.util.Path;

/**
 * Unit tests for Path functionality.
 */
public class PathTest {
    Path p = new Path();

    /**
     * Test that file DFS finds a file that exists.
     */
    @Test
    public void fileDFSTest() {
        File f = p.fileDFS("", "Path.java");
        try {
            assertEquals("Path.java", f.getName());
        } catch (NullPointerException e) {
            assertFalse("File should not be null", true);
        }
    }

    /**
     * Test with a file that does not exist, expect null
     */
    @Test
    public void fileDoesNotExistTest() {
        File f = p.fileDFS("", "Korvar12345678910.cool");
        assertEquals(null, f);
    }
}