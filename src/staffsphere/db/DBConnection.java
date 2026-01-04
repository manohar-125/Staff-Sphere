package staffsphere.db;
import java.sql.*;
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/staffsphere";
    private static final String USER = "root";
    private static final String PASSWORD = "passwd@123";

    public static Connection getConnection(){
        try{
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            return con;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
