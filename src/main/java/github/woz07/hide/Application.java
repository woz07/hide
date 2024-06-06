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
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Application.java
 * This is the entry class which is also the main GUI class.
 *
 * @author woz07
 */

public class Application extends JFrame {
    // File structure
    // f = File
    private static final File fRoot = new File(System.getenv("APPDATA"), "Hide");
    private static final File fConfig = new File(fRoot, "config.txt");
    
    // External libraries
    private static final Configuration config = new Configuration(fConfig);
    private static final BCipher bCipher = new BCipher();
    
    private final String version = "1.0";
    
    // Keys
    private ArrayList<Byte> keys = new ArrayList<>();
    
    private final JPanel container;
    private final GridBagConstraints gbc;
    // t = TextArea
    // u = Undo
    // s = ScrollPane
    private final JTextArea tInput;
    private final UndoManager uInputManager;
    private final JScrollPane sInputScroll;
    private final JButton bSubmit;
    private final JTextArea tOutput;
    private final UndoManager uOutputManager;
    private final JScrollPane sOutputScroll;
    
    public Application() {
        // Setting up application
        setTitle("Hide");
        setPreferredSize(new Dimension(600, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Setting up favicon
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/favicon.png")));

        
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
        JMenuItem iVersion = new JMenuItem("Version: " + version);
        iVersion.setEnabled(false);
        mHelp.add(iAbout);
        mHelp.add(iHelp);
        mHelp.add(iIssue);
        mHelp.add(iVersion);
        
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
        tInput.setLineWrap(true);
        tInput.setRows(10);
        tInput.setColumns(40);
        sInputScroll = new JScrollPane(tInput);
        sInputScroll.setPreferredSize(new Dimension(500, 135));
        
        // u = Undo
        uInputManager = new UndoManager();
        tInput.getDocument().addUndoableEditListener(e -> uInputManager.addEdit(e.getEdit()));
        // a = Action
        Action aInputAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (uInputManager.canUndo()) {
                        uInputManager.undo();
                    }
                } catch (CannotUndoException ignore) {}
            }
        };
        Action aInputRedo = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (uInputManager.canRedo()) {
                        uInputManager.redo();
                    }
                } catch (CannotRedoException ignore) {}
            }
        };
        // Bind actions to tInput
        // Ctrl + Z
        tInput.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), "Undo");
        tInput.getActionMap().put("Undo", aInputAction);
        // Ctrl + Shift + Z
        tInput.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK), "Redo");
        tInput.getActionMap().put("Redo", aInputRedo);
        
        // Submit
        bSubmit = new JButton("Cipher");
        bSubmit.addActionListener(hSubmit());
        
        // Output
        tOutput = new JTextArea();
        tOutput.setText("Ciphered text appears here...");
        tOutput.setLineWrap(true);
        tOutput.setRows(10);
        tOutput.setColumns(40);
        sOutputScroll = new JScrollPane(tOutput);
        sOutputScroll.setPreferredSize(new Dimension(500, 135));
    
        // u = Undo
        uOutputManager = new UndoManager();
        tInput.getDocument().addUndoableEditListener(e -> uOutputManager.addEdit(e.getEdit()));
        // a = Action
        Action aOutputUndo = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (uOutputManager.canUndo()) {
                        uOutputManager.undo();
                    }
                } catch (CannotUndoException ignore) {}
            }
        };
        Action aOutputRedo = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (uOutputManager.canRedo()) {
                        uOutputManager.redo();
                    }
                } catch (CannotRedoException ignore) {}
            }
        };
        // Bind actions to tOutput
        // Ctrl + Z
        tOutput.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), "Undo");
        tOutput.getActionMap().put("Undo", aOutputUndo);
        // Ctrl + Shift + Z
        tOutput.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK), "Redo");
        tOutput.getActionMap().put("Redo", aOutputRedo);
        
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
                new WError(null, "[1.1] Unable to create folder called 'Hide' within %APPDATA%");
            }
        }
        
        // Setup fConfig
        if (!fConfig.exists()) {
            try {
                if (!fConfig.createNewFile()) {
                    new WError(null, "[1.2] Unable to create config.txt in: " + fRoot.getAbsolutePath());
                }
            } catch (IOException e) {
                new WError(null,
                        "[1.3] Unable to create config.txt in " + fRoot.getAbsolutePath() + ". \n" +
                                "Error: " + e.getMessage()
                );
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
        
        // Full set up if it's all empty/ first time set up
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
    }
    
    /**
     * Remove element at index in keys
     * @param index The index to remove element at
     */
    public void kRemove(int index) {
        keys.remove(index);
    }
    
    /**
     * Function to clear keys
     */
    public void kClear() {
        keys.clear();
    }
    
    // Listener for bSubmit
    // h = Handle
    
    private ActionListener hSubmit() {
        return e -> {
            // Check if keys isn't empty
            if (keys == null) {
                new WError(this, "[2.1] Keys must not be empty");
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
                new WError(this, "[2.2] Unable to set keys. <br> Error: " + ex.getMessage());
            }
            // Get text then convert and put it into output
            // Only do if keys isn't null
            if (keys != null && !keys.isEmpty()) {
                tOutput.setText(bCipher.cipher(tInput.getText()));
            }
            bCipher.flush();
        };
    }
    
    // Listeners for JMenuItem(s)
    // h = Handle
    
    // File menu listeners
    
    private ActionListener hModifyKeys() {
        return e -> SwingUtilities.invokeLater(() -> new WModifyKeys(this));
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
        String link = "https://www.github.com/woz07/hide#About";
        return e -> {
            try {
                URI uri = new URI(link);
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(uri);
                }
            } catch (URISyntaxException | IOException u) {
                new WError(this, "[3.1] Unable to open link in browser: <br>" + link);
            }
        };
    }
    
    private ActionListener hHelp() {
        String link = "https://www.github.com/woz07/hide#Help";
        return e -> {
            try {
                URI uri = new URI(link);
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(uri);
                }
            } catch (URISyntaxException | IOException u) {
                new WError(this, "[3.1] Unable to open link in browser: <br>" + link);
            }
        };
    }
    
    private ActionListener hIssue() {
        String link = "https://www.github.com/woz07/hide/issues";
        return e -> {
            try {
                URI uri = new URI(link);
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(uri);
                }
            } catch (URISyntaxException | IOException u) {
                new WError(this, "[3.1] Unable to open link in browser: <br>" + link);
            }
        };
    }
}