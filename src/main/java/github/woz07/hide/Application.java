package github.woz07.hide;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import github.woz07.BCipher;
import github.woz07.liteconfig.Configuration;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Application extends JFrame {
    // File structure
    private static final File fRoot = new File(System.getenv("APPDATA"), "Hide");
    private static final File fConfig = new File(fRoot, "config.txt");
    
    // External libraries
    private static final Configuration config = new Configuration(fConfig);
    private static final BCipher bCipher = new BCipher();
    
    private final JMenuBar menu;
    
    private final JPanel container;
    private final JScrollPane sPane;
    public Application() {
        
        // Setting up application
        setTitle("Hide");
        setPreferredSize(new Dimension(600, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Setting up menu ~ m = menu, i = item
        JMenu mFile = new JMenu("File");
        JMenuItem iAddKeys = new JMenuItem("Add key(s)");
        JMenuItem iRemoveKeys = new JMenuItem("Remove key(s)");
        JMenuItem iViewKeys = new JMenuItem("View key(s)");
        JMenuItem iMode = new JMenuItem("Mode");
        JMenuItem iExit = new JMenuItem("Exit");
        mFile.add(iAddKeys);
        mFile.add(iRemoveKeys);
        mFile.add(iViewKeys);
        mFile.add(iMode);
        mFile.add(iExit);
        
        JMenu mHelp = new JMenu("Help");
        JMenuItem iAbout = new JMenuItem("About");
        JMenuItem iHelp = new JMenuItem("Help page");
        JMenuItem iIssue = new JMenuItem("Found an issue?");
        mHelp.add(iAbout);
        mHelp.add(iHelp);
        mHelp.add(iIssue);
        
        
        // Finalizing menu
        menu = new JMenuBar();
        menu.add(mFile);
        menu.add(mHelp);
        setJMenuBar(menu);
        
        // Setting up components
        sPane = new JScrollPane();
        
        
        // Finalizing components
        container = new JPanel(new GridLayout());
        
        // Finalizing application
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        // Setup fRoot
        if (!fRoot.exists()) {
            if (!fRoot.mkdir()) {
                System.err.println("Unable to create folder called 'Hide' within %APPDATA%");
            }
        }
        
        // Setup fConfig
        if (!fConfig.exists()) {
            try {
                if (!fConfig.createNewFile()) {
                    System.err.println("Unable to create config.txt in: " + fRoot.getAbsolutePath());
                }
            } catch (IOException e) {
                System.err.println("Unable to create config.txt in " + fRoot.getAbsolutePath() + "\nErr: " + e);
            }
        }
        // Ensure fConfig isn't empty
        String[] keys = {"MODE"};
        int count = 0;
        for (String key : keys) {
            if (config.get(key) == null) {
                count++;
            }
        }
        if (count == keys.length) {
            // File is empty, so setup
            try {
                config.set("MODE", "light");
            } catch (Exception ignore) {}
        }
        
        // Set mode
        if (config.get("MODE").equals("light")) {
            FlatMacLightLaf.setup();
        } else {
            FlatMacDarkLaf.setup();
        }
        
        // Run application
        SwingUtilities.invokeLater(Application::new);
    }
}
