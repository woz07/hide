package github.woz07.hide;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import github.woz07.BCipher;
import github.woz07.liteconfig.Configuration;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Application extends JFrame {
    private static final Configuration config = new Configuration(new File("config.txt"));
    private static final BCipher bCipher = new BCipher();
    
//    private final JPanel container;
    public Application() {
        setTitle("Hide");
        setPreferredSize(new Dimension(600, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        // Set mode
        if (config.get("MODE").equals("light")) {
            FlatMacDarkLaf.setup();
        } else {
            FlatMacDarkLaf.setup();
        }
        
        // Run application
        SwingUtilities.invokeLater(Application::new);
    }
}
