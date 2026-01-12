package staffsphere.ui;

import javax.swing.*;
import java.awt.*;
import staffsphere.model.User;
import staffsphere.util.CurrentUser;

public class DashboardUI extends JFrame{

    private User loggedInUser;

    public DashboardUI(User user) {
        this.loggedInUser = user;

        setTitle("Staff Sphere - Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getUsername() + " (" + user.getRole() + ")", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel buttonPanel = new JPanel();
        JButton btnLogout = new JButton("Logout");

        String role = loggedInUser.getRole();

        // Show different buttons based on role
        if(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("hr")){
            JButton btnManageStaff = new JButton("Manage Staff");
            buttonPanel.add(btnManageStaff);
            
            btnManageStaff.addActionListener(e -> {
                new StaffManagementUI().setVisible(true);
            });
        } else if(role.equalsIgnoreCase("staff")) {
            JButton btnMyProfile = new JButton("My Profile");
            buttonPanel.add(btnMyProfile);
            
            btnMyProfile.addActionListener(e -> {
                new EmployeeSelfServiceUI().setVisible(true);
            });
        }

        buttonPanel.add(btnLogout);

        add(welcomeLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnLogout.addActionListener(e ->{
            CurrentUser.set(null);
            new LoginUI().setVisible(true);
            dispose();
        });
    }
}
