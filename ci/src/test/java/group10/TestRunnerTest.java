package group10;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

/**
 * Unit tests for Path functionality.
 */
public class TestRunnerTest {
    TestRunner tr;
    File root;
    File child1;
    File child2;
    File child3;
    File directory1;
    File directory2;

    @Before
    public void initialiseFiles() {
        tr = new TestRunner();

        root = Mockito.mock(File.class);
        child1 = Mockito.mock(File.class);
        child2 = Mockito.mock(File.class);
        child3 = Mockito.mock(File.class);
        directory1 = Mockito.mock(File.class);
        directory2 = Mockito.mock(File.class);

        when(child1.getName()).thenReturn("test.class");
        when(child2.getName()).thenReturn("notWanted.txt");
        when(child3.getName()).thenReturn("test3.class");
        when(directory1.getName()).thenReturn("dir1");
        when(directory2.getName()).thenReturn("dir2");
        when(directory1.isDirectory()).thenReturn(true);
        when(directory2.isDirectory()).thenReturn(true);
    }

    /**
     * Test if "findClasses" finds classes in 
     * root directory.
     */
    @Test
    public void findClassFilesSameDirectory() {
        File[] rootFiles = {
            child1,
            child2,
            child3
        };
        when(root.listFiles()).thenReturn(rootFiles);
        
        List<File> classFiles = new ArrayList<File>();
        tr.findClasses(classFiles, root);
        assertEquals(2, classFiles.size());
        assertEquals("test.class", classFiles.get(0).getName());
        assertEquals("test3.class", classFiles.get(1).getName());
    }

    /**
     * Test if "findClasses" finds classes in 
     * a directory placed in root.
     */
    @Test
    public void findClassFilesInsideDirectory() {
        File[] rootFiles = {
            directory1,
            directory2
        };
        when(root.listFiles()).thenReturn(rootFiles);

        File[] directory1Files = {
            child1,
            child2
        };
        when(directory1.listFiles()).thenReturn(directory1Files);

        File[] directory2Files = {
            child3
        };
        when(directory2.listFiles()).thenReturn(directory2Files);

        List<File> classFiles = new ArrayList<File>();
        tr.findClasses(classFiles, root);
        assertEquals(2, classFiles.size());
        assertEquals("test.class", classFiles.get(0).getName());
        assertEquals("test3.class", classFiles.get(1).getName());
    }

    /**
     * Test if "findClasses" handles correctly
     * when there are no .class files.
     */
    @Test
    public void findClassNoClassFilesInsideDirectory() {
        when(root.listFiles()).thenReturn(null);
        List<File> classFiles = new ArrayList<File>();
        tr.findClasses(classFiles, root);
    }

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /**
     * Test that convertClasses ignores non Test
     * classes.
     */
    @Test
    public void covertClassesTest() throws IOException {
        folder.newFolder("subfolder");
        folder.newFile("AbortBest.class");

        List<File> classFiles = new ArrayList<File>();
        tr.findClasses(classFiles, folder.getRoot());   
        tr.convertToClasses(classFiles, folder.getRoot());
    }

    /**
     * Test that it tries to load .class, expect
     * error since it is a mock class.
     */
    @Test(expected = AssertionError.class)
    public void convertClassesExpectClassNotFound() throws IOException {
        File f = folder.newFile("Test.class");
        List<File> classFiles = new ArrayList<File>();
        tr.findClasses(classFiles, folder.getRoot());   
        tr.convertToClasses(classFiles, folder.getRoot());
        f.delete();
    }
}