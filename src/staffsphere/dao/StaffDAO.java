package staffsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import staffsphere.db.DBConnection;
import staffsphere.model.Staff;
import staffsphere.model.User;
import staffsphere.util.CurrentUser;

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
    public boolean addStaff(Staff staff, String password, String role){
        String sql = "INSERT INTO employees (emp_code, name, email, phone, department, designation, salary, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection con = null;
        try{
            con = DBConnection.getConnection();
            con.setAutoCommit(false); // Start transaction

            // First, create user account
            UserDAO userDAO = new UserDAO();
            if(userDAO.userExists(staff.getEmpCode())) {
                con.rollback();
                return false; // Username already exists
            }

            if(!userDAO.createUser(staff.getEmpCode(), password, role)) {
                con.rollback();
                return false;
            }

            // Then create employee record
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, staff.getEmpCode());
            ps.setString(2, staff.getName());
            ps.setString(3, staff.getEmail());
            ps.setString(4, staff.getPhone());
            ps.setString(5, staff.getDepartment());
            ps.setString(6, staff.getDesignation());
            ps.setDouble(7, staff.getSalary());
            ps.setInt(8, CurrentUser.get().getUserId());
            ps.executeUpdate();
            ps.close();

            con.commit();
            return true;

        }catch (SQLException e){
            try {
                if(con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if(con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Staff> getAllStaff(boolean includeInactive) {
        List<Staff> list = new ArrayList<>();

        String sql = includeInactive
                ? "SELECT emp_id, emp_code, name, email, phone, department, designation, salary, status, created_by, created_at, updated_at FROM employees"
                : "SELECT emp_id, emp_code, name, email, phone, department, designation, salary, status, created_by, created_at, updated_at FROM employees WHERE status = 'active'";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while(rs.next()) {
                Staff s = new Staff();
                s.setId(rs.getInt("emp_id"));
                s.setEmpCode(rs.getString("emp_code"));
                s.setName(rs.getString("name"));
                s.setEmail(rs.getString("email"));
                s.setPhone(rs.getString("phone"));
                s.setDepartment(rs.getString("department"));
                s.setDesignation(rs.getString("designation"));
                s.setSalary(rs.getDouble("salary"));
                s.setStatus(rs.getString("status"));
                s.setCreatedBy(rs.getInt("created_by"));
                s.setCreatedAt(rs.getTimestamp("created_at"));
                s.setUpdatedAt(rs.getTimestamp("updated_at"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean deleteStaff(int id){
        User currentUser = CurrentUser.get();
        Staff staff = getStaffById(id);
        
        if(staff == null) return false;

        // Check permissions
        if(currentUser.getRole().equalsIgnoreCase("hr")) {
            // HR can only delete staff, not other HR or admin
            User targetUser = new UserDAO().getUserByUsername(staff.getEmpCode());
            if(targetUser != null && !targetUser.getRole().equalsIgnoreCase("staff")) {
                return false; // HR cannot delete admin or other HR
            }
        }

        String sql = "UPDATE employees SET status = 'inactive' WHERE emp_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            // Also update user status
            new UserDAO().updateUserStatus(staff.getEmpCode(), "inactive");
            return true;

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean restoreStaff(int id) {
        User currentUser = CurrentUser.get();
        Staff staff = getStaffById(id);
        
        if(staff == null) return false;

        // Check permissions
        if(currentUser.getRole().equalsIgnoreCase("hr")) {
            // HR can only restore staff, not other HR or admin
            User targetUser = new UserDAO().getUserByUsername(staff.getEmpCode());
            if(targetUser != null && !targetUser.getRole().equalsIgnoreCase("staff")) {
                return false;
            }
        }

        String sql = "UPDATE employees SET status = 'active' WHERE emp_id = ?";

        try (Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            // Also update user status
            new UserDAO().updateUserStatus(staff.getEmpCode(), "active");
            return true;

        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStaff(Staff staff){
        User currentUser = CurrentUser.get();
        
        // Check permissions
        if(currentUser.getRole().equalsIgnoreCase("hr")) {
            // HR can only update staff and themselves
            User targetUser = new UserDAO().getUserByUsername(staff.getEmpCode());
            if(targetUser != null && targetUser.getRole().equalsIgnoreCase("admin")) {
                return false; // HR cannot update admin
            }
            if(targetUser != null && targetUser.getRole().equalsIgnoreCase("hr") 
                && !targetUser.getUsername().equals(currentUser.getUsername())) {
                return false; // HR can only update their own HR record
            }
        }

        String sql = "UPDATE employees SET emp_code = ?, name = ?, email = ?, phone = ?, department = ?, designation = ?, salary = ? WHERE emp_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, staff.getEmpCode());
            ps.setString(2, staff.getName());
            ps.setString(3, staff.getEmail());
            ps.setString(4, staff.getPhone());
            ps.setString(5, staff.getDepartment());
            ps.setString(6, staff.getDesignation());
            ps.setDouble(7, staff.getSalary());
            ps.setInt(8, staff.getId());
            ps.executeUpdate();
            return true;

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public List<Staff> searchStaff(String keyword) {
        List<Staff> list = new ArrayList<>();

        String sql = "SELECT emp_id, emp_code, name, email, phone, department, designation, salary, status, created_by, created_at, updated_at FROM employees WHERE status = 'active' AND (emp_code LIKE ? OR name LIKE ? OR email LIKE ? OR designation LIKE ? OR department LIKE ?)";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            String value = "%" + keyword + "%";
            ps.setString(1, value);
            ps.setString(2, value);
            ps.setString(3, value);
            ps.setString(4, value);
            ps.setString(5, value);

            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    Staff s = new Staff();
                    s.setId(rs.getInt("emp_id"));
                    s.setEmpCode(rs.getString("emp_code"));
                    s.setName(rs.getString("name"));
                    s.setEmail(rs.getString("email"));
                    s.setPhone(rs.getString("phone"));
                    s.setDepartment(rs.getString("department"));
                    s.setDesignation(rs.getString("designation"));
                    s.setSalary(rs.getDouble("salary"));
                    s.setStatus(rs.getString("status"));
                    s.setCreatedBy(rs.getInt("created_by"));
                    s.setCreatedAt(rs.getTimestamp("created_at"));
                    s.setUpdatedAt(rs.getTimestamp("updated_at"));
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

    public Staff getStaffById(int id) {
        String sql = "SELECT emp_id, emp_code, name, email, phone, department, designation, salary, status, created_by, created_at, updated_at FROM employees WHERE emp_id = ?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    Staff s = new Staff();
                    s.setId(rs.getInt("emp_id"));
                    s.setEmpCode(rs.getString("emp_code"));
                    s.setName(rs.getString("name"));
                    s.setEmail(rs.getString("email"));
                    s.setPhone(rs.getString("phone"));
                    s.setDepartment(rs.getString("department"));
                    s.setDesignation(rs.getString("designation"));
                    s.setSalary(rs.getDouble("salary"));
                    s.setStatus(rs.getString("status"));
                    s.setCreatedBy(rs.getInt("created_by"));
                    s.setCreatedAt(rs.getTimestamp("created_at"));
                    s.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return s;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Staff getStaffByEmpCode(String empCode) {
        String sql = "SELECT emp_id, emp_code, name, email, phone, department, designation, salary, status, created_by, created_at, updated_at FROM employees WHERE emp_code = ?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, empCode);

            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    Staff s = new Staff();
                    s.setId(rs.getInt("emp_id"));
                    s.setEmpCode(rs.getString("emp_code"));
                    s.setName(rs.getString("name"));
                    s.setEmail(rs.getString("email"));
                    s.setPhone(rs.getString("phone"));
                    s.setDepartment(rs.getString("department"));
                    s.setDesignation(rs.getString("designation"));
                    s.setSalary(rs.getDouble("salary"));
                    s.setStatus(rs.getString("status"));
                    s.setCreatedBy(rs.getInt("created_by"));
                    s.setCreatedAt(rs.getTimestamp("created_at"));
                    s.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return s;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public void updateStaffSelfService(Staff staff) {
        String sql = "UPDATE employees SET email = ?, phone = ? WHERE emp_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, staff.getEmail());
            ps.setString(2, staff.getPhone());
            ps.setInt(3, staff.getId());
            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
