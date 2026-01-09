package staffsphere.ui;

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
            String userName = tUserName.getText();
            String passWord = new String(tPassword.getPassword());

            System.out.println("Username:" + userName);
            System.out.println("Password:" + passWord);
        });
    }
}
