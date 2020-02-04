package group10;

import java.sql.*;
import java.util.Properties;

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
     * @return
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

    public void close() throws SQLException {
        conn.close();
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DatabaseHandler dbh = new DatabaseHandler();
        dbh.connect("ci");
        System.out.println(dbh.conn);
        int rs = dbh.addBuild(0, "pendingggggggggg", "johan", "master");
        System.out.println(rs);
        //PreparedStatement stmt = con.prepareStatement("insert into Emp values(?,?)");  
        //stmt.setInt(1,101);//1 specifies the first parameter in the query  
        //stmt.setString(2,"Ratan"); 
    }
}