package github.woz07.hide;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import github.woz07.BCipher;
import github.woz07.exceptions.BCipherKeyException;
import github.woz07.exceptions.BCipherNullException;
import github.woz07.exceptions.BCipherSizeException;
import github.woz07.liteconfig.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Application extends JFrame {
    // File structure
    private static final File fRoot = new File(System.getenv("APPDATA"), "Hide");
    private static final File fConfig = new File(fRoot, "config.txt");
    
    // External libraries
    private static final Configuration config = new Configuration(fConfig);
    private static final BCipher bCipher = new BCipher();
    
    // Keys
//    private byte[] keys = new byte[255];
    private ArrayList<Byte> keys = new ArrayList<>();
    
    private final JPanel container;
    private final GridBagConstraints gbc;
    // t = TextArea
    // s = ScrollPane
    // l = Label
    private final JTextArea tInput;
    private final JScrollPane sInputScroll;
    private final JButton bSubmit;
    private final JTextArea tOutput;
    private final JScrollPane sOutputScroll;
    public Application() {
        // Setting up application
        setTitle("Hide");
        setPreferredSize(new Dimension(600, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Setting up menus ~ m = menu, i = item
        // File menu
        JMenu mFile = new JMenu("File");
        JMenuItem iModifyKeys = new JMenuItem("Modify key(s)");
        iModifyKeys.addActionListener(hModifyKeys());
        JMenuItem iMode = new JMenuItem("Mode");
        iMode.addActionListener(hMode());
        JMenuItem iExit = new JMenuItem("Exit");
        iExit.addActionListener(hExit());
        mFile.add(iModifyKeys);
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
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Input
        tInput = new JTextArea();
        tInput.setText("Enter text to cipher...");
        tInput.setRows(10);
        tInput.setColumns(40);
        sInputScroll = new JScrollPane(tInput);
        sInputScroll.setPreferredSize(new Dimension(500, 135));
        
        // Submit
        bSubmit = new JButton("Cipher");
        bSubmit.addActionListener(hSubmit());
        
        // Output
        tOutput = new JTextArea();
        tOutput.setText("Ciphered text appears here...");
        tOutput.setRows(10);
        tOutput.setColumns(40);
        sOutputScroll = new JScrollPane(tOutput);
        sOutputScroll.setPreferredSize(new Dimension(500, 135));
        
        // Finalizing components
        container.add(sInputScroll, gbc);
        gbc.gridy++;
        container.add(bSubmit, gbc);
        gbc.gridy++;
        container.add(sOutputScroll, gbc);
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
        
        // Full set up
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
    
    // Getter for keys
    public ArrayList<Byte> getKeys() {
        return keys;
    }
    
    // Useful methods for keys, used in WModifyKeys.java
    // k = keys
    
    /**
     * Add element to keys
     * @param key The key to add
     */
    public void kAdd(byte key) {
        keys.add(key);
        System.out.println(keys);
    }
    
    /**
     * Remove element at index in keys
     * @param index The index to remove element at
     */
    public void kRemove(int index) {
        keys.remove(index);
    }
    
    // Listener for bSubmit
    // h = Handle
    
    private ActionListener hSubmit() {
        return e -> {
            // Check if keys isn't empty
            if (keys == null) {
                System.err.println("Keys mustn't be empty");
                return;
            }
            // Set keys always before ciphering
            try {
                // Convert keys to byte[]
                byte[] convert = new byte[keys.size()];
                for (int i = 0; i < keys.size(); i++) {
                    convert[i] = keys.get(i);
                }
                bCipher.setKey(convert);
            } catch (BCipherNullException | BCipherKeyException | BCipherSizeException ex) {
                System.err.println("Unable to set keys.\nErr: " + ex.getMessage());
            }
            // Get text then convert and put it into output
            tOutput.setText(bCipher.cipher(tInput.getText()));
            bCipher.flush();
        };
    }
    
    // Listeners for JMenuItem(s)
    // h = Handle
    
    // File menu listeners
    
    private ActionListener hModifyKeys() {
        return e -> {
            SwingUtilities.invokeLater(() -> new WModifyKeys(this));
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
            try {
                URI uri = new URI("https://www.github.com/woz07/hide#About");
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(uri);
                }
            } catch (URISyntaxException | IOException u) {
                u.printStackTrace();
            }
        };
    }
    
    private ActionListener hHelp() {
        return e -> {
            try {
                URI uri = new URI("https://www.github.com/woz07/hide#Help");
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
