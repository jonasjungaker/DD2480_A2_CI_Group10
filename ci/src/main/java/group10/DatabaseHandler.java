package group10;

import java.sql.*;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

public class DatabaseHandler {
    Connection conn;

    /**
     * Connect to the database.
     * @param database name of the db 
     */
    public void connect(String database) {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "korvar123");
        properties.setProperty("useSSL", "false");
        properties.setProperty("autoReconnect", "true");
        try {
            conn =
               DriverManager.getConnection("jdbc:mysql://localhost/" + database, properties);
        
            // Do something with the Connection
        } catch (SQLException e) {
            // handle any errorss
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Insert a build into the database.
     * @param elapsed time taken to run tests
     * @param status pending, pass or success
     * @param author who made the push
     * @param branch branch the tests was run on
     * @return build_id, id of build
     */
    public int addBuild(String status, String author, String branch) {
        if (conn != null) {
            String query = "insert into build (date, status, author, branch) values (?,?,?,?)";
            java.util.Date date = new java.util.Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            try {
                PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                preparedStmt.setString(1, timestamp.toString());
                preparedStmt.setString(2, status);
                preparedStmt.setString(3, author);
                preparedStmt.setString(4, branch);

                preparedStmt.execute();
                // get the ID of the inserted row, needed 
                // when we want to add the tests to this entry
                ResultSet generatedKeys = preparedStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return (int) generatedKeys.getLong(1);
                }
            } catch (SQLException e) {
                //e.printStackTrace();
                System.out.println("Invalid insert query in addBuild");
            }
        }
        return -1;
    }

    /**
     * Add test results to a build.
     * @param buildID id of the build to add tests to
     * @param results from the test run
     * @return true if successful
     */
    public boolean addTestsToBuild(int buildID, JSONObject results) {
        if (conn != null) {
            // add the failed tests
            JSONArray failed = (JSONArray) results.get("failed");
            String query = "insert into test (build_id, name, message, elapsed, class) values (?,?,?,?,?)";
            for (int i = 0; i < failed.length(); i++) {
                JSONObject result = failed.getJSONObject(i);
                System.out.println(result);
                String name = (String) result.get("name");
                double elapsed = (double) result.get("time");
                String className = (String) result.get("classname");
                String cause = (String) result.get("cause");

                try {
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1, buildID);
                    preparedStmt.setString(2, name);
                    preparedStmt.setString(3, cause);
                    preparedStmt.setDouble(4, elapsed);
                    preparedStmt.setString(5, className);

                    preparedStmt.execute();
                } catch (SQLException e) {
                    System.out.println("Adding failed tests failed");
                    return false;
                }
            }

            // add the passed tests
            JSONArray passed = (JSONArray) results.get("succeded");
            query = "insert into test (build_id, name, elapsed, class) values (?,?,?,?)";
            for (int i = 0; i < passed.length(); i++) {
                JSONObject result = passed.getJSONObject(i);
                String name = (String) result.get("name");
                double elapsed = (double) result.get("time");
                String className = (String) result.get("classname");

                try {
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1, buildID);
                    preparedStmt.setString(2, name);
                    preparedStmt.setDouble(3, elapsed);
                    preparedStmt.setString(4, className);

                    preparedStmt.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Adding passed tests failed");
                    return false;
                }
            }

            // add the meta data for the tests
            query = "UPDATE build SET " + 
                        "elapsed = ?," +
                        "status = ?," +
                        "number_passed = ?," +
                        "number_failed = ?" + 
                        "WHERE build_id = ?";
            try {
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setDouble(1, (double)results.get("time"));
                if ((boolean)results.get("success")) {
                    preparedStmt.setString(2, "passed");
                } else {
                    preparedStmt.setString(2, "failed");
                }
                preparedStmt.setInt(3, (int)results.get("number_success"));
                preparedStmt.setInt(4, (int)results.get("number_failed"));
                preparedStmt.setInt(5, buildID);

                preparedStmt.execute(); 
            } catch (SQLException e) {
                System.out.println("Failed update of build meta data");
                return false;
            }

            return true;
        }
        return false;
    }

    /**
     * Method to get builds and the corresponding 
     * @param from, starting position
     * @param number, number of builds to fetch
     * @return JSONArray of JSONObjects
     */
    public JSONArray getBuilds(int from, int number) {
        if (conn != null) {
            String query = "select * from build order by date desc limit ?,?";

            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, from);
                stmt.setInt(2, number);
                ResultSet rs = stmt.executeQuery(); 

                JSONArray builds = new JSONArray();
                while (rs.next()) {
                    JSONObject build = new JSONObject();
                    build.put("buildID", rs.getInt("build_id"));
                    build.put("date", rs.getDate("date"));
                    build.put("status", rs.getString("status"));
                    build.put("author", rs.getString("author"));
                    build.put("branch", rs.getString("branch"));
                    build.put("elapsed", rs.getDouble("elapsed"));
                    build.put("number_passed", rs.getInt("number_passed"));
                    build.put("number_failed", rs.getInt("number_failed"));
                    builds.put(build);
                }
                rs.close();
                return builds;
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to get build list");
            }
        }
        return new JSONArray();
    }

    /**
     * Get information about a specific build.
     * @param buildID, id of build
     * @return JSONObject of build
     */
    public JSONObject getBuild(int buildID) {
        if (conn != null) {
            try {
                String query = "select * from build where build_id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, buildID);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                // build data
                JSONObject build = new JSONObject();
                build.put("buildID", rs.getInt("build_id"));
                build.put("date", rs.getDate("date"));
                build.put("status", rs.getString("status"));
                build.put("author", rs.getString("author"));
                build.put("branch", rs.getString("branch"));
                build.put("elapsed", rs.getDouble("elapsed"));
                build.put("number_passed", rs.getInt("number_passed"));
                build.put("number_failed", rs.getInt("number_failed"));

                // passed test data
                JSONArray passedTests = new JSONArray();
                JSONArray failedTests = new JSONArray();
                String getPassed = "select * from test where build_id = ?";
                stmt = conn.prepareStatement(getPassed);
                stmt.setInt(1, buildID);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    JSONObject test = new JSONObject();
                    test.put("name", rs.getString("name"));
                    test.put("elapsed", rs.getString("elapsed"));
                    test.put("className", rs.getString("class"));
                    String message = rs.getString("message");
                    System.out.println(message);
                    if (message == null) {
                        passedTests.put(test);
                    } else {
                        failedTests.put(test);
                    }
                }

                build.put("passed_tests", passedTests);
                build.put("failed_tests", failedTests);
                
                return build; 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return new JSONObject();
    }

    public void close() throws SQLException {
        conn.close();
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DatabaseHandler dbh = new DatabaseHandler();
        dbh.connect("test");
        JSONObject results = new JSONObject("{\"succeded\":[{\"name\":\"fileDFSTest\",\"test_number\":0},{\"name\":\"fileDoesNotExistTest\",\"test_number\":1},{\"name\":\"resultToJsonPassingTest\",\"test_number\":0},{\"name\":\"resultToJsonTest\",\"test_number\":1},{\"name\":\"parseTest\",\"test_number\":0},{\"name\":\"parseEmptyTest\",\"test_number\":1},{\"name\":\"parseExamleString\",\"test_number\":2},{\"name\":\"expectSQLerror\",\"test_number\":0},{\"name\":\"addBuildTest\",\"test_number\":1},{\"name\":\"cloneTest\",\"test_number\":0},{\"name\":\"branchNotExistTest\",\"test_number\":1},{\"name\":\"covertClassesTest\",\"test_number\":0},{\"name\":\"convertClassesExpectClassNotFound\",\"test_number\":1},{\"name\":\"findClassFilesInsideDirectory\",\"test_number\":2},{\"name\":\"findClassNoClassFilesInsideDirectory\",\"test_number\":3},{\"name\":\"findClassFilesSameDirectory\",\"test_number\":4},{\"name\":\"parseTest\",\"test_number\":0},{\"name\":\"parseEmptyTest\",\"test_number\":1}],\"number_failed\":1,\"success\":false,\"number_success\":18,\"failed\":[{\"name\":\"shouldAnswerWithTrue\",\"cause\":\"\\n    java.lang.AssertionError\\n\\tat group10.AppTest.shouldAnswerWithTrue(AppTest.java:18)\\n\\n  \",\"test_number\":0}]}");
        dbh.addTestsToBuild(4, results);
        //PreparedStatement stmt = con.prepareStatement("insert into Emp values(?,?)");  
        //stmt.setInt(1,101);//1 specifies the first parameter in the query  
        //stmt.setString(2,"Ratan"); 
    }
}