package group10;

import java.sql.*;
import java.util.Properties;

public class DatabaseHandler {
    Connection conn;

    public DatabaseHandler() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");  
    }

    public void connect() throws ClassNotFoundException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "korvar123");
        properties.setProperty("useSSL", "false");
        properties.setProperty("autoReconnect", "true");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn =
               DriverManager.getConnection("jdbc:mysql://localhost/ci", properties);
        
            // Do something with the Connection
        } catch (SQLException | ClassNotFoundException e) {
            // handle any errorss
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public void close() throws SQLException {
        conn.close();
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DatabaseHandler dbh = new DatabaseHandler();
        dbh.connect();
        Statement stmt = dbh.conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Build");
        System.out.println(rs);
        //PreparedStatement stmt = con.prepareStatement("insert into Emp values(?,?)");  
        //stmt.setInt(1,101);//1 specifies the first parameter in the query  
        //stmt.setString(2,"Ratan"); 
    }
}