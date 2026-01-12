package staffsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import staffsphere.db.DBConnection;
import staffsphere.model.Staff;

public class StaffDAO {
    private static StaffDAO instance;

    private StaffDAO(){
    }

    public static StaffDAO getInstance() {
        if(instance == null) {
            instance = new StaffDAO();
        }
        return instance;
    }
    public void addStaff(Staff staff){
        String sql = "INSERT INTO employees (name, email, designation, salary) VALUES (?, ?, ?, ?)";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, staff.getName());
            ps.setString(2, staff.getEmail());
            ps.setString(3, staff.getRole());
            ps.setDouble(4, staff.getSalary());

            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<Staff> getAllStaff(boolean includeInactive) {
        List<Staff> list = new ArrayList<>();

        String sql = includeInactive
                ? "SELECT emp_id, name, email, designation, salary, status FROM employees"
                : "SELECT emp_id, name, email, designation, salary, status FROM employees WHERE status = 'active'";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while(rs.next()) {
                Staff s = new Staff();
                s.setId(rs.getInt("emp_id"));
                s.setName(rs.getString("name"));
                s.setEmail(rs.getString("email"));
                s.setRole(rs.getString("designation"));
                s.setSalary(rs.getDouble("salary"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deleteStaff(int id){
        String sql = "UPDATE employees SET status = 'inactive' WHERE emp_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void restoreStaff(int id) {
        String sql = "UPDATE employees SET status = 'active' WHERE emp_id = ?";

        try (Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStaff(Staff staff){
        String sql = "UPDATE employees SET name = ?, email = ?, designation = ?, salary = ? WHERE emp_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, staff.getName());
            ps.setString(2, staff.getEmail());
            ps.setString(3, staff.getRole());
            ps.setDouble(4, staff.getSalary());
            ps.setInt(5, staff.getId());
            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<Staff> searchStaff(String keyword) {
        List<Staff> list = new ArrayList<>();

        String sql = "SELECT emp_id, name, email, designation, salary FROM employees WHERE status = 'active' AND (name LIKE ? OR email LIKE ? OR designation LIKE ?)";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            String value = "%" + keyword + "%";
            ps.setString(1, value);
            ps.setString(2, value);
            ps.setString(3, value);

            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    Staff s = new Staff();
                    s.setId(rs.getInt("emp_id"));
                    s.setName(rs.getString("name"));
                    s.setEmail(rs.getString("email"));
                    s.setRole(rs.getString("designation"));
                    s.setSalary(rs.getDouble("salary"));
                    list.add(s);
                }
            } catch (SQLException e){
                e.printStackTrace();
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }

}
