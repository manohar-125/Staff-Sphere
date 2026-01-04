package staffsphere.main;
import javax.swing.SwingUtilities;
import staffsphere.ui.LoginUI;

public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(()-> {
            LoginUI login = new LoginUI();
            login.setVisible(true);
        });
    }
}
