package staffsphere.ui;

import javax.swing.*;
import java.awt.*;
import staffsphere.dao.StaffDAO;
import staffsphere.dao.UserDAO;
import staffsphere.model.Staff;
import staffsphere.model.User;
import staffsphere.util.CurrentUser;
import staffsphere.util.PasswordUtil;

public class EmployeeSelfServiceUI extends JFrame {

    private StaffDAO staffDAO;
    private UserDAO userDAO;
    private Staff currentStaff;
    private User currentUser;

    private JTextField empCodeField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField departmentField;
    private JTextField designationField;
    private JTextField salaryField;
    private JTextField statusField;

    public EmployeeSelfServiceUI() {
        currentUser = CurrentUser.get();
        
        if(currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please login first");
            dispose();
            return;
        }

        staffDAO = StaffDAO.getInstance();
        userDAO = new UserDAO();

        currentStaff = staffDAO.getStaffByEmpCode(currentUser.getUsername());

        if(currentStaff == null) {
            JOptionPane.showMessageDialog(this, "Employee record not found");
            dispose();
            return;
        }

        setTitle("My Profile - " + currentUser.getUsername());
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Employee Code:"), gbc);
        
        empCodeField = new JTextField(20);
        empCodeField.setEditable(false);
        empCodeField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 1;
        formPanel.add(empCodeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Name:"), gbc);
        
        nameField = new JTextField(20);
        nameField.setEditable(false);
        nameField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Email:"), gbc);
        
        emailField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Phone:"), gbc);
        
        phoneField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Department:"), gbc);
        
        departmentField = new JTextField(20);
        departmentField.setEditable(false);
        departmentField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 1;
        formPanel.add(departmentField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Designation:"), gbc);
        
        designationField = new JTextField(20);
        designationField.setEditable(false);
        designationField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 1;
        formPanel.add(designationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Salary:"), gbc);
        
        salaryField = new JTextField(20);
        salaryField.setEditable(false);
        salaryField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 1;
        formPanel.add(salaryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(new JLabel("Status:"), gbc);
        
        statusField = new JTextField(20);
        statusField.setEditable(false);
        statusField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 1;
        formPanel.add(statusField, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton btnUpdate = new JButton("Update Profile");
        JButton btnChangePassword = new JButton("Change Password");
        JButton btnClose = new JButton("Close");

        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnChangePassword);
        buttonPanel.add(btnClose);

        add(buttonPanel, BorderLayout.SOUTH);

        loadStaffData();

        btnUpdate.addActionListener(e -> updateProfile());
        btnChangePassword.addActionListener(e -> changePassword());
        btnClose.addActionListener(e -> dispose());
    }

    private void loadStaffData() {
        empCodeField.setText(currentStaff.getEmpCode());
        nameField.setText(currentStaff.getName());
        emailField.setText(currentStaff.getEmail());
        phoneField.setText(currentStaff.getPhone() != null ? currentStaff.getPhone() : "");
        departmentField.setText(currentStaff.getDepartment() != null ? currentStaff.getDepartment() : "");
        designationField.setText(currentStaff.getDesignation() != null ? currentStaff.getDesignation() : "");
        salaryField.setText(String.valueOf(currentStaff.getSalary()));
        statusField.setText(currentStaff.getStatus() != null ? currentStaff.getStatus() : "active");
    }

    private void updateProfile() {
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if(email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email is required");
            return;
        }

        if(!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format");
            return;
        }

        currentStaff.setEmail(email);
        currentStaff.setPhone(phone);

        staffDAO.updateStaffSelfService(currentStaff);
        JOptionPane.showMessageDialog(this, "Profile updated successfully");
    }

    private void changePassword() {
        JPasswordField oldPasswordField = new JPasswordField(20);
        JPasswordField newPasswordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);

        Object[] form = {
                "Current Password:", oldPasswordField,
                "New Password:", newPasswordField,
                "Confirm Password:", confirmPasswordField
        };

        int choice = JOptionPane.showConfirmDialog(this, form, "Change Password", JOptionPane.OK_CANCEL_OPTION);
        if(choice != JOptionPane.OK_OPTION) {
            return;
        }

        String oldPassword = new String(oldPasswordField.getPassword()).trim();
        String newPassword = new String(newPasswordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        if(oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required");
            return;
        }

        // Verify old password - support both plain text and BCrypt
        boolean passwordMatches = false;
        if(currentUser.getPassword().startsWith("$2a$") || currentUser.getPassword().startsWith("$2b$")) {
            // BCrypt hashed password
            passwordMatches = PasswordUtil.verifyPassword(oldPassword, currentUser.getPassword());
        } else {
            // Plain text password (legacy)
            passwordMatches = oldPassword.equals(currentUser.getPassword());
        }

        if(!passwordMatches) {
            JOptionPane.showMessageDialog(this, "Current password is incorrect");
            return;
        }

        if(!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "New passwords do not match");
            return;
        }

        if(newPassword.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters");
            return;
        }

        // Update password in database with BCrypt hash
        try {
            String hashedPassword = PasswordUtil.hashPassword(newPassword);
            java.sql.Connection con = staffsphere.db.DBConnection.getConnection();
            java.sql.PreparedStatement ps = con.prepareStatement("UPDATE users SET password = ? WHERE user_id = ?");
            ps.setString(1, hashedPassword);
            ps.setInt(2, currentUser.getUserId());
            ps.executeUpdate();
            ps.close();
            con.close();

            currentUser.setPassword(hashedPassword);
            JOptionPane.showMessageDialog(this, "Password changed successfully");
        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error changing password");
        }
    }
}
