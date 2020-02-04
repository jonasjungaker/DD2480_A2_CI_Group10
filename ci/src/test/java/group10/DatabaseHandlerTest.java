package group10;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for parse functionality.
 */
public class DatabaseHandlerTest {
    DatabaseHandler dbh = new DatabaseHandler();

    @Before
    public void setup() {
        dbh.connect("test");
        //reset();
    }

    @After
    public void clean() throws SQLException {
        dbh.close();
    }

    /**
     * Reset the tables
     */
    public void reset() {
        try {
            System.out.println("Reset database");
            Statement q = dbh.conn.createStatement();
            q.execute("SET FOREIGN_KEY_CHECKES=0");
            q.execute("truncate table build");
            q.execute("ALTER TABLE build AUTO_INCREMENT=0");
            q.execute("SET FOREIGN_KEY_CHECKS=1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the number of rows in table
     */
    public int getRows(String table) {
        try {
            Statement trunc = dbh.conn.createStatement();
            ResultSet rs = trunc.executeQuery("select * from " + table);
            rs.last();
            return rs.getRow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Test that it is possible to add a build
     */
    @Test
    public void addBuildTest() {
        // do not run tests if no db connection
        if (dbh.conn != null) {
            int currentRow = getRows("build");
            int index  = dbh.addBuild(0, "pending", "test", "muster");
            assertEquals(currentRow+1, index);
        } 
    }

    /**
     * Test too long input, should not add it
     */
    @Test
    public void expectNoAddedBuild() {
        // do not run tests if no db connection
        if (dbh.conn != null) {
            // 16 characters over allowed 15 for status
            int currentRow = getRows("build");
            dbh.addBuild(0, "pendingggggggggg", "test", "muster");
            int currentRow2 = getRows("build");
            assertEquals(currentRow, currentRow2);
        } 
    }

    /**
     * Test adding one passed and one failed test to
     * a build.
     */
    @Test
    public void addTestsToBuild() {
        // do not run tests if no db connection
        if (dbh.conn != null) {
            // 16 characters over allowed 15 for status
            int currentRowPassed = getRows("passedTest");
            int currentRowFailed = getRows("failedTest");
            int buildID = dbh.addBuild(0, "pending", "test", "tehe");
            JSONObject results = new JSONObject("{\"succeded\":[{\"name\":\"fileDFSTest\",\"test_number\":0}],\"number_failed\":1,\"success\":false,\"number_success\":1,\"failed\":[{\"name\":\"shouldAnswerWithTrue\",\"cause\":\"\\n    java.lang.AssertionError\\n\\tat group10.AppTest.shouldAnswerWithTrue(AppTest.java:18)\\n\\n  \",\"test_number\":0}]}");
            dbh.addTestsToBuild(buildID, results);
            int currentRow2Passed = getRows("passedTest");
            int currentRow2Failed = getRows("failedTest");
            assertEquals(currentRowPassed+1, currentRow2Passed);
            assertEquals(currentRowFailed+1, currentRow2Failed);
        } 
    }

    /**
     * Test to pass a result where no tests
     * failed or passed!
     */
    @Test
    public void addTestsEmptyTests() {
        // do not run tests if no db connection
        if (dbh.conn != null) {
            // 16 characters over allowed 15 for status
            int currentRowPassed = getRows("passedTest");
            int currentRowFailed = getRows("failedTest");
            int buildID = dbh.addBuild(0, "pending", "test", "tehe");
            JSONObject results = new JSONObject("{\"succeded\":[],\"number_failed\":0,\"success\":false,\"number_success\":0,\"failed\":[]}");
            dbh.addTestsToBuild(buildID, results);
            int currentRow2Passed = getRows("passedTest");
            int currentRow2Failed = getRows("failedTest");
            assertEquals(currentRowPassed, currentRow2Passed);
            assertEquals(currentRowFailed, currentRow2Failed);
        } 
    }



}
