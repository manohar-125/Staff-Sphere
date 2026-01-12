package staffsphere.dao;
import staffsphere.db.DBConnection;
import staffsphere.model.User;
import staffsphere.util.PasswordUtil;
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

    public boolean createUser(String username, String password, String role) {
        String sql = "INSERT INTO users (username, password, role, status) VALUES (?, ?, ?, 'active')";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, PasswordUtil.hashPassword(password));
            ps.setString(3, role);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUserStatus(String username, String newStatus) {
        // Prevent admin from being deactivated
        User user = getUserByUsername(username);
        if(user != null && user.getRole().equalsIgnoreCase("admin")) {
            return false; // Cannot change admin status
        }

        String sql = "UPDATE users SET status = ? WHERE username = ?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setString(2, username);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(String username) {
        // Prevent admin from being deleted
        User user = getUserByUsername(username);
        if(user != null && user.getRole().equalsIgnoreCase("admin")) {
            return false;
        }

        String sql = "DELETE FROM users WHERE username = ?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean userExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
