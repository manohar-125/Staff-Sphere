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
    }
}
