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
        setSize(700, 400);
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

        String[] columns = {"ID", "Name", "Email", "Role", "Salary"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col){
                return col != 0;
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

        add(bottomPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e-> {
            JTextField nameField = new JTextField();
            JTextField emailField = new JTextField();
            JTextField roleField = new JTextField();
            JTextField salaryField = new JTextField();

            Object[] form = {
                    "Name:", nameField,
                    "Email:", emailField,
                    "Role:", roleField,
                    "Salary:", salaryField
            };

            int choice = JOptionPane.showConfirmDialog(this, form, "Add Staff", JOptionPane.OK_CANCEL_OPTION);
            if(choice != JOptionPane.OK_OPTION){
                return;
            }

            if(nameField.getText().isBlank() || emailField.getText().isBlank() || roleField.getText().isBlank() || salaryField.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "All fields are required");
                return;
            }

            if( !emailField.getText().contains("@")) {
                JOptionPane.showMessageDialog(this, "Invalid email");
                return;
            }

            double salry;
            try {
                salry = Double.parseDouble(salaryField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid salary value");
                return;
            }

            Staff s = new Staff();
            s.setName(nameField.getText());
            s.setEmail(emailField.getText());
            s.setRole(roleField.getText());
            s.setSalary(salry);

            staffDAO.addStaff(s);
            loadStaffData();
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();

            if(row == -1){
                JOptionPane.showMessageDialog(this, "Please select a staff to delete");
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure! you want to delete?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if( choice != JOptionPane.YES_OPTION){
                return;
            }
            staffDAO.deleteStaff(id);
            loadStaffData();
        });

        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();

            if(row == -1){
                JOptionPane.showMessageDialog(this, "Please select a staff to update");
                return;
            }

            int id = (int) tableModel.getValueAt(row, 0);
            String name = tableModel.getValueAt(row, 1).toString();
            String email = tableModel.getValueAt(row, 2).toString();
            String role = tableModel.getValueAt(row, 3).toString();
            String salary = tableModel.getValueAt(row, 4).toString();

            JTextField nameField = new JTextField();
            JTextField emailField = new JTextField();
            JTextField roleField = new JTextField();
            JTextField salaryField = new JTextField();

            nameField.setText(name);
            emailField.setText(email);
            roleField.setText(role);
            salaryField.setText(salary);

            Object[] form = {
                    "Name:", nameField,
                    "Email:", emailField,
                    "Role:", roleField,
                    "Salary:", salaryField
            };

            int choice = JOptionPane.showConfirmDialog(this, form, "Update Staff", JOptionPane.OK_CANCEL_OPTION);
            if(choice != JOptionPane.OK_OPTION){
                return;
            }


            Staff s = new Staff();

            if(nameField.getText().isBlank() || emailField.getText().isBlank() || roleField.getText().isBlank() || salaryField.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "All fields are required");
                return;
            }

            s.setId(id);
            s.setName(nameField.getText());

            if( !emailField.getText().contains("@")) {
                JOptionPane.showMessageDialog(this, "Invalid email");
                return;
            }
            s.setEmail((emailField.getText()));

            s.setRole(roleField.getText());

            double salry;
            try {
                salry = Double.parseDouble(salaryField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid salary value");
                return;
            }
            s.setSalary(salry);

            staffDAO.updateStaff(s);
            loadStaffData();
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
                        s.getName(),
                        s.getEmail(),
                        s.getRole(),
                        s.getSalary()
                });
            }
        });

        showInactiveCheckBox.addActionListener( e -> {
            boolean showInactive = showInactiveCheckBox.isSelected();
            List<Staff> list = staffDAO.getAllStaff(showInactive);

            tableModel.setRowCount(0);
            for (Staff s : list) {
                tableModel.addRow(new Object[]{
                        s.getId(),
                        s.getName(),
                        s.getEmail(),
                        s.getRole(),
                        s.getSalary()
                });
            }
        });

        loadStaffData();
    }

    private void loadStaffData() {
        tableModel.setRowCount(0);

        for (Staff s : staffDAO.getAllStaff(false)) {
            tableModel.addRow(new Object[]{
                    s.getId(), s.getName(), s.getEmail(), s.getRole(), s.getSalary()
            });
        }
    }
}
