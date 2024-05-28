package github.woz07.hide;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import github.woz07.BCipher;
import github.woz07.liteconfig.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Application extends JFrame {
    // File structure
    private static final File fRoot = new File(System.getenv("APPDATA"), "Hide");
    private static final File fConfig = new File(fRoot, "config.txt");
    
    // External libraries
    private static final Configuration config = new Configuration(fConfig);
    private static final BCipher bCipher = new BCipher();
    
    private final JPanel container;
    private final GridBagConstraints gbc;
    public Application() {
        
        // Setting up application
        setTitle("Hide");
        setPreferredSize(new Dimension(600, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Setting up menus ~ m = menu, i = item
        // File menu
        JMenu mFile = new JMenu("File");
        JMenuItem iAddKeys = new JMenuItem("Add key(s)");
        iAddKeys.addActionListener(hAddKeys());
        JMenuItem iRemoveKeys = new JMenuItem("Remove key(s)");
        iRemoveKeys.addActionListener(hRemoveKeys());
        JMenuItem iViewKeys = new JMenuItem("View key(s)");
        iViewKeys.addActionListener(hViewKeys());
        JMenuItem iMode = new JMenuItem("Mode");
        iMode.addActionListener(hMode());
        JMenuItem iExit = new JMenuItem("Exit");
        iExit.addActionListener(hExit());
        mFile.add(iAddKeys);
        mFile.add(iRemoveKeys);
        mFile.add(iViewKeys);
        mFile.add(iMode);
        mFile.add(iExit);
        
        // Help menu
        JMenu mHelp = new JMenu("Help");
        JMenuItem iAbout = new JMenuItem("About");
        iAbout.addActionListener(hAbout());
        JMenuItem iHelp = new JMenuItem("Help page");
        iHelp.addActionListener(hHelp());
        JMenuItem iIssue = new JMenuItem("Found an issue?");
        iIssue.addActionListener(hIssue());
        mHelp.add(iAbout);
        mHelp.add(iHelp);
        mHelp.add(iIssue);
        
        // Finalizing menu
        JMenuBar menu = new JMenuBar();
        menu.add(mFile);
        menu.add(mHelp);
        setJMenuBar(menu);
        
        // Setting up components
        container = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        
        // Finalizing components
        add(container, BorderLayout.CENTER);
        
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
            FlatLaf.setup(new FlatMacLightLaf());
        } else {
            FlatLaf.setup(new FlatMacDarkLaf());
        }
    
        // Run application
        SwingUtilities.invokeLater(Application::new);
    }
    
    // Listeners for JMenuItem(s)
    // h = Handle
    
    // File menu listeners
    
    private ActionListener hAddKeys() {
        return e -> {
        
        };
    }
    
    private ActionListener hRemoveKeys() {
        return e -> {
        
        };
    }
    
    private ActionListener hViewKeys() {
        return e -> {
        
        };
    }
    
    private ActionListener hMode() {
        return e -> {
            // Get current mode and change it
            if (config.get("MODE").equals("light")) {
                config.updateValue("MODE", "dark");
                FlatLaf.setup(new FlatMacDarkLaf());
            } else {
                config.updateValue("MODE", "light");
                FlatLaf.setup(new FlatMacLightLaf());
            }
            SwingUtilities.updateComponentTreeUI(this);
        };
    }
    
    private ActionListener hExit() {
        return e -> {
            // Clean up and then close
            bCipher.flush();
            this.dispose();
            System.exit(0);
        };
    }
    
    // Help menu listeners
    
    private ActionListener hAbout() {
        return e -> {
        
        };
    }
    
    private ActionListener hHelp() {
        return e -> {
            try {
                URI uri = new URI("https://www.github.com/woz07/hide#help");
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(uri);
                }
            } catch (URISyntaxException | IOException u) {
                u.printStackTrace();
            }
        };
    }
    
    private ActionListener hIssue() {
        return e -> {
            try {
                URI uri = new URI("https://www.github.com/woz07/hide/issues");
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(uri);
                }
            } catch (URISyntaxException | IOException u) {
                u.printStackTrace();
            }
        };
    }
}
