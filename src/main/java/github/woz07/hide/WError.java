package github.woz07.hide;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * WError.java
 * This is the error displaying class, which displays the
 * error in GUI format for the user to read.
 *
 * @author woz07
 */

public class WError extends JFrame {
    // l = Label
    private final JLabel lError;
    // b = Button
    private final JButton bAccept;
    private final JButton bHelp;
    
    public WError(JFrame parent, String message) {
        setTitle("Hide ~ Error");
        setPreferredSize(new Dimension(320, 200));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
        // Setting up favicon
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/favicon.png")));
        
        // Setting up components
        lError = new JLabel("<html>" + message + "</html>", SwingConstants.CENTER);
        
        bAccept = new JButton("Okay");
        bAccept.addActionListener(e -> dispose());
        bHelp = new JButton("Help");
        bHelp.addActionListener(e -> {
            String link = "https://www.github.com/woz07/hide#Errors";
            try {
                URI uri = new URI(link);
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(uri);
                }
            } catch (URISyntaxException | IOException u) {
                new WError(this, "[3.1] Unable to open link in browser: " + link);
            }
        });
        
        // Finalizing components
        // p = Panel
        JPanel pTop = new JPanel(new BorderLayout());
        pTop.add(lError, BorderLayout.CENTER);
        
        JPanel pBottom = new JPanel();
        pBottom.add(bAccept);
        pBottom.add(bHelp);
        
        // Add components
        setLayout(new BorderLayout());
        add(pTop, BorderLayout.CENTER);
        add(pBottom, BorderLayout.SOUTH);
        
        // Finalizing
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }
}