package staffsphere.dao;
import staffsphere.db.DBConnection;
import staffsphere.model.User;
import java.sql.*;

public class UserDAO {

    public User getUserByUsername(String username){
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        con = DBConnection.getConnection();
        if(con == null){
            return null;
        }

        String sql = "SELECT user_id, username, password, role, status FROM users WHERE username = ?";

        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if(rs.next()){
                User newUser = new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("status"));
                return newUser;
            }else{
                return null;
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
