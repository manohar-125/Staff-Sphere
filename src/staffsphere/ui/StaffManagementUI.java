package staffsphere.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import staffsphere.dao.StaffDAO;
import staffsphere.model.Staff;
import staffsphere.util.CurrentUser;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

public class StaffManagementUI extends JFrame {

    private StaffDAO staffDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    public StaffManagementUI(){

        if(!CurrentUser.isAdminOrHr()) {
            JOptionPane.showMessageDialog(this, "Access Denied");
            dispose();
            return;
        }

        setTitle("Staff Management");
        setSize(1100, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        staffDAO = StaffDAO.getInstance();

        JPanel topPanel = new JPanel(new BorderLayout());

        JTextField searchField = new JTextField();
        JButton btnSearch = new JButton("Search");

        topPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(btnSearch, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        JCheckBox showInactiveCheckBox = new JCheckBox("Show Inactive");
        topPanel.add(showInactiveCheckBox, BorderLayout.SOUTH);

        String[] columns = {"ID", "Emp Code", "Name", "Email", "Phone", "Department", "Designation", "Salary", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col){
                return false;
            }
        };
        table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        JButton btnAdd = new JButton("Add");
        bottomPanel.add(btnAdd);

        JButton btnUpdate = new JButton("Update");
        bottomPanel.add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        bottomPanel.add(btnDelete);

        JButton btnRestore = new JButton("Restore");
        btnRestore.setEnabled(false);
        bottomPanel.add(btnRestore);

        add(bottomPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e-> {
            JTextField empCodeField = new JTextField();
            JTextField nameField = new JTextField();
            JTextField emailField = new JTextField();
            JTextField phoneField = new JTextField();
            JTextField departmentField = new JTextField();
            JTextField designationField = new JTextField();
            JTextField salaryField = new JTextField();
            JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"staff", "hr"});
            JPasswordField passwordField = new JPasswordField();
            JPasswordField confirmPasswordField = new JPasswordField();

            Object[] form = {
                    "Emp Code (Username):", empCodeField,
                    "Name:", nameField,
                    "Email:", emailField,
                    "Phone:", phoneField,
                    "Department:", departmentField,
                    "Designation:", designationField,
                    "Salary:", salaryField,
                    "Role:", roleComboBox,
                    "Password:", passwordField,
                    "Confirm Password:", confirmPasswordField
            };

            int choice = JOptionPane.showConfirmDialog(this, form, "Add Employee", JOptionPane.OK_CANCEL_OPTION);
            if(choice != JOptionPane.OK_OPTION){
                return;
            }

            if(empCodeField.getText().isBlank() || nameField.getText().isBlank() || emailField.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Emp Code, Name, and Email are required");
                return;
            }

            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if(password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password is required");
                return;
            }

            if(!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match");
                return;
            }

            if(password.length() < 6) {
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters");
                return;
            }

            if(!emailField.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(this, "Invalid email format");
                return;
            }

            double salary;
            try {
                salary = Double.parseDouble(salaryField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid salary value");
                return;
            }

            Staff s = new Staff();
            s.setEmpCode(empCodeField.getText().trim());
            s.setName(nameField.getText().trim());
            s.setEmail(emailField.getText().trim());
            s.setPhone(phoneField.getText().trim());
            s.setDepartment(departmentField.getText().trim());
            s.setDesignation(designationField.getText().trim());
            s.setSalary(salary);

            String role = (String) roleComboBox.getSelectedItem();
            boolean success = staffDAO.addStaff(s, password, role);
            
            if(success) {
                JOptionPane.showMessageDialog(this, "Employee and user account created successfully");
                loadStaffData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add employee. Username may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();

            if(row == -1){
                JOptionPane.showMessageDialog(this, "Please select a staff to delete");
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);
            String empCode = tableModel.getValueAt(row, 1).toString();
            
            int choice = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to deactivate " + empCode + "?", 
                "Confirm Deactivate", 
                JOptionPane.YES_NO_OPTION);
            if(choice != JOptionPane.YES_OPTION){
                return;
            }
            
            boolean success = staffDAO.deleteStaff(id);
            if(success) {
                JOptionPane.showMessageDialog(this, "Employee deactivated successfully");
                loadStaffData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to deactivate. You may not have permission.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();

            if(row == -1){
                JOptionPane.showMessageDialog(this, "Please select a staff to update");
                return;
            }

            int id = (int) tableModel.getValueAt(row, 0);
            String empCode = tableModel.getValueAt(row, 1).toString();
            String name = tableModel.getValueAt(row, 2).toString();
            String email = tableModel.getValueAt(row, 3).toString();
            String phone = tableModel.getValueAt(row, 4) != null ? tableModel.getValueAt(row, 4).toString() : "";
            String department = tableModel.getValueAt(row, 5) != null ? tableModel.getValueAt(row, 5).toString() : "";
            String designation = tableModel.getValueAt(row, 6) != null ? tableModel.getValueAt(row, 6).toString() : "";
            String salary = tableModel.getValueAt(row, 7).toString();

            JTextField empCodeField = new JTextField();
            JTextField nameField = new JTextField();
            JTextField emailField = new JTextField();
            JTextField phoneField = new JTextField();
            JTextField departmentField = new JTextField();
            JTextField designationField = new JTextField();
            JTextField salaryField = new JTextField();

            empCodeField.setText(empCode);
            nameField.setText(name);
            emailField.setText(email);
            phoneField.setText(phone);
            departmentField.setText(department);
            designationField.setText(designation);
            salaryField.setText(salary);

            Object[] form = {
                    "Emp Code:", empCodeField,
                    "Name:", nameField,
                    "Email:", emailField,
                    "Phone:", phoneField,
                    "Department:", departmentField,
                    "Designation:", designationField,
                    "Salary:", salaryField
            };

            int choice = JOptionPane.showConfirmDialog(this, form, "Update Staff", JOptionPane.OK_CANCEL_OPTION);
            if(choice != JOptionPane.OK_OPTION){
                return;
            }

            if(empCodeField.getText().isBlank() || nameField.getText().isBlank() || emailField.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Emp Code, Name, and Email are required");
                return;
            }

            if(!emailField.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(this, "Invalid email format");
                return;
            }

            double salaryValue;
            try {
                salaryValue = Double.parseDouble(salaryField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid salary value");
                return;
            }

            Staff s = new Staff();
            s.setId(id);
            s.setEmpCode(empCodeField.getText().trim());
            s.setName(nameField.getText().trim());
            s.setEmail(emailField.getText().trim());
            s.setPhone(phoneField.getText().trim());
            s.setDepartment(departmentField.getText().trim());
            s.setDesignation(designationField.getText().trim());
            s.setSalary(salaryValue);

            boolean success = staffDAO.updateStaff(s);
            if(success) {
                JOptionPane.showMessageDialog(this, "Staff updated successfully");
                loadStaffData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update. You may not have permission.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRestore.addActionListener(e -> {
            int row = table.getSelectedRow();

            if(row == -1){
                JOptionPane.showMessageDialog(this, "Please select an inactive staff to restore");
                return;
            }
            
            String status = tableModel.getValueAt(row, 8).toString();
            if(!status.equalsIgnoreCase("inactive")) {
                JOptionPane.showMessageDialog(this, "Selected employee is already active");
                return;
            }
            
            int id = (int) tableModel.getValueAt(row, 0);
            String empCode = tableModel.getValueAt(row, 1).toString();
            
            int choice = JOptionPane.showConfirmDialog(this, 
                "Restore " + empCode + " to active status?", 
                "Confirm Restore", 
                JOptionPane.YES_NO_OPTION);
            if(choice != JOptionPane.YES_OPTION){
                return;
            }
            
            boolean success = staffDAO.restoreStaff(id);
            if(success) {
                JOptionPane.showMessageDialog(this, "Employee restored successfully");
                showInactiveCheckBox.setSelected(true);
                loadStaffData(true);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to restore. You may not have permission.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSearch.addActionListener( e -> {
            String keyword = searchField.getText().trim();

            if(keyword.isEmpty()) {
                loadStaffData();
                return;
            }

            List<Staff> result = staffDAO.searchStaff(keyword);
            tableModel.setRowCount(0);

            for (Staff s : result){
                tableModel.addRow(new Object[]{
                        s.getId(),
                        s.getEmpCode(),
                        s.getName(),
                        s.getEmail(),
                        s.getPhone(),
                        s.getDepartment(),
                        s.getDesignation(),
                        s.getSalary(),
                        s.getStatus()
                });
            }
        });

        showInactiveCheckBox.addActionListener( e -> {
            boolean showInactive = showInactiveCheckBox.isSelected();
            btnRestore.setEnabled(showInactive);
            loadStaffData(showInactive);
        });

        loadStaffData(false);
    }

    private void loadStaffData() {
        loadStaffData(false);
    }

    private void loadStaffData(boolean includeInactive) {
        tableModel.setRowCount(0);

        for (Staff s : staffDAO.getAllStaff(includeInactive)) {
            tableModel.addRow(new Object[]{
                    s.getId(),
                    s.getEmpCode(),
                    s.getName(),
                    s.getEmail(),
                    s.getPhone(),
                    s.getDepartment(),
                    s.getDesignation(),
                    s.getSalary(),
                    s.getStatus()
            });
        }
    }
}
