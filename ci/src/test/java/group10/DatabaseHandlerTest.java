package group10;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for parse functionality.
 */
public class DatabaseHandlerTest {
    DatabaseHandler dbh = new DatabaseHandler();
    List<Integer> buildIds = new ArrayList<Integer>();

    @Before
    public void setup() {
        dbh.connect("test");
        //reset();
    }

    @After
    public void clean() throws SQLException {
        for (Integer buildID : buildIds) {
            removeBuild(buildID);
        }
        if (dbh.conn != null) {
            dbh.close();
        } 
    }

    /**
     * Remove a build from the database
     * @param buildID build to remove
     */
    public void removeBuild(int buildID) {
        try {
            Statement trunc = dbh.conn.createStatement();
            trunc.executeUpdate("delete from build where build_id = " + buildID);
            trunc.executeUpdate("delete from passedTest where build_id = " + buildID);
            trunc.executeUpdate("delete from failedTest where build_id = " + buildID);
            
        } catch (SQLException e) {
            System.out.println("Failed to remove build: " + buildID);
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
            int index  = dbh.addBuild("pending", "popego", "muster");
            int currentRow2 = getRows("build");
            buildIds.add(index);
            assertEquals(currentRow+1, currentRow2);
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
            dbh.addBuild("pendingggggggggg", "test", "muster");
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
            int currentRowPassed = getRows("passedTest");
            int currentRowFailed = getRows("failedTest");
            int buildID = dbh.addBuild("pending", "test", "tehe");
            buildIds.add(buildID);
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
            int currentRowPassed = getRows("passedTest");
            int currentRowFailed = getRows("failedTest");
            int buildID = dbh.addBuild("pending", "test", "tehe");
            buildIds.add(buildID);
            JSONObject results = new JSONObject("{\"succeded\":[],\"number_failed\":0,\"success\":false,\"number_success\":0,\"failed\":[]}");
            dbh.addTestsToBuild(buildID, results);
            int currentRow2Passed = getRows("passedTest");
            int currentRow2Failed = getRows("failedTest");
            assertEquals(currentRowPassed, currentRow2Passed);
            assertEquals(currentRowFailed, currentRow2Failed);
        } 
    }

    /**
     * Test get build list, check that offset
     * and number of builds to fetch works.
     */
    @Test
    public void getBuildsTest() {
        // do not run tests if no db connection
        if (dbh.conn != null) {
            int buildID = dbh.addBuild("pending", "pepe", "cool");
            buildIds.add(buildID);

            JSONArray builds = dbh.getBuilds(0, 1);
            assertEquals(1, builds.length());
            JSONObject build = (JSONObject) builds.get(0);
            assertEquals("pepe", build.getString("author"));

            buildID = dbh.addBuild("pending", "pepega", "korv");
            buildIds.add(buildID);

            builds = dbh.getBuilds(0, 2);
            assertEquals(2, builds.length());
            System.out.println(builds);
            build = (JSONObject) builds.get(1);
            assertEquals("pepega", build.getString("author"));
        } 
    }



}
