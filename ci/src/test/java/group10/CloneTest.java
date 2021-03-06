package group10;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cloning a repository
 */
public class CloneTest {

  File cloneDirectoryPath;

  @Before
  public void setUp() {
    cloneDirectoryPath = new File("../gitclones");
  }

  @After
  public void tearDown() throws IOException {
    GithubController.tearDown(cloneDirectoryPath);
  }

  /**
   * Tests cloning a repository where the repo and branch exist
   * Should be true when checking if directory exist
   * @throws IOException
   */
  @Test
  public void cloneTest() throws IOException
    {
        String repoUrl = "https://github.com/jonasjungaker/DD2480_A2_CI_Group10";
        boolean result = GithubController.cloneRepository(repoUrl, "refs/heads/master", cloneDirectoryPath);
        assertTrue(cloneDirectoryPath.exists() && result);
    }

  /**
   * Tests cloning a branch that does not exist
   * Directory should not exist and false should be returned
   * @throws IOException
   */
  @Test
  public void branchNotExistTest() throws IOException
    {
        String repoUrl = "https://github.com/jonasjungaker/DD2480_A2_CI_Group10";
        boolean result = GithubController.cloneRepository(repoUrl, "refs/heads/aNonExistingBranch", cloneDirectoryPath);
        assertFalse(cloneDirectoryPath.exists() || result);
        
    }
}
