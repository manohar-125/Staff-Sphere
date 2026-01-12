package staffsphere.ui;

import javax.swing.*;
import java.awt.*;
import staffsphere.model.User;

public class DashboardUI extends JFrame{

    private User loggedInUser;

    public DashboardUI(User user) {
        this.loggedInUser = user;

        setTitle("Staff Sphere - Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getUsername(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnManageStaff = new JButton("Manage Staff");
        JButton btnLogout = new JButton("Logout");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnManageStaff);
        bottomPanel.add(btnLogout);

        add(welcomeLabel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        String role = loggedInUser.getRole();

        if(role.equalsIgnoreCase("staff")){
            btnManageStaff.setEnabled(false);
        }

        btnManageStaff.addActionListener(e -> {
            new StaffManagementUI().setVisible(true);
        });

        btnLogout.addActionListener(e ->{
            new LoginUI().setVisible(true);
            dispose();
        });
    }
}
