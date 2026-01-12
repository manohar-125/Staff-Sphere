package staffsphere.ui;
import staffsphere.dao.UserDAO;
import staffsphere.model.User;
import staffsphere.util.CurrentUser;

import javax.swing.*;
import java.awt.*;


public class LoginUI extends JFrame{
    JTextField tUserName;
    JPasswordField tPassword;

    public LoginUI(){
        setTitle("Staff Sphere - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblUserName = new JLabel("Username:");
        tUserName = new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(lblUserName, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(tUserName, gbc);

        JLabel lblPassword = new JLabel("Password:");
        tPassword = new JPasswordField(15);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblPassword, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(tPassword, gbc);

        JButton btnLogin = new JButton("Login");
        JButton btnClear = new JButton("Clear");

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(btnLogin, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        add(btnClear, gbc);

        btnClear.addActionListener(e-> {
            tUserName.setText("");
            tPassword.setText("");
        });

        btnLogin.addActionListener(e-> {
            String userName = tUserName.getText().trim();
            String passWord = new String(tPassword.getPassword()).trim();

            if(userName.isEmpty() || passWord.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and Password are required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            UserDAO userDao = new UserDAO();
            User user = userDao.getUserByUsername(userName);

            if(user == null){
                JOptionPane.showMessageDialog(this, "Invalid Username or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!passWord.equals(user.getPassword())){
                JOptionPane.showMessageDialog(this,"Invalid Username or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!user.getStatus().equalsIgnoreCase("active")) {
                JOptionPane.showMessageDialog(this, "Account is Disabled. Please contact admin.", "Access Denied", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String role = user.getRole();

            JOptionPane.showMessageDialog(this, "Login Successful! Role: " + role, "Success", JOptionPane.INFORMATION_MESSAGE);

            if(user != null) {
                JOptionPane.showMessageDialog(this, "Login Successful");

                user = userDao.getUserByUsername(userName);

                if(user != null && user.getPassword().equals(passWord)) {
                    CurrentUser.set(user);
                    new DashboardUI(user).setVisible(true);
                    dispose();
                }

            }else{
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
            }
        });
    }
}
