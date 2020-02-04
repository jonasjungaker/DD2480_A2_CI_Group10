package group10;

import java.sql.*;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

public class DatabaseHandler {
    Connection conn;

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
    public int addBuild(double elapsed, String status, String author, String branch) {
        if (conn != null) {
            String query = "insert into build (date, elapsed, status, author, branch) values (?,?,?,?,?)";
            java.util.Date date = new java.util.Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            try {
                PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                preparedStmt.setString(1, timestamp.toString());
                preparedStmt.setDouble(2, elapsed);
                preparedStmt.setString(3, status);
                preparedStmt.setString(4, author);
                preparedStmt.setString(5, branch);

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
            JSONArray failed = (JSONArray) results.get("failed");
            String query = "insert into failedTest (build_id, name, message) values (?,?,?)";

            for (int i = 0; i < failed.length(); i++) {
                JSONObject result = failed.getJSONObject(i);
                String name = (String) result.get("name");
                String cause = (String) result.get("cause");

                try {
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1, buildID);
                    preparedStmt.setString(2, name);
                    preparedStmt.setString(3, cause);

                    preparedStmt.execute();
                } catch (SQLException e) {
                    System.out.println("Adding failed tests failed");
                    return false;
                }
            }

            JSONArray passed = (JSONArray) results.get("succeded");
            query = "insert into passedTest (build_id, name) values (?,?)";

            for (int i = 0; i < passed.length(); i++) {
                JSONObject result = passed.getJSONObject(i);
                String name = (String) result.get("name");

                try {
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1, buildID);
                    preparedStmt.setString(2, name);

                    preparedStmt.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Adding passed tests failed");
                    return false;
                }
            }
            return true;
        }
        return false;
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