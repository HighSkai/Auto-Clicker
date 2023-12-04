import javax.swing.*;
import java.awt.*;

public class ClickerApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ClickerFrame();
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
