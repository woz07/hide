package github.woz07.hide;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
        
        // Setting up components
        lError = new JLabel(message);
        
        bAccept = new JButton("Okay");
        bAccept.addActionListener(e -> dispose());
        bHelp = new JButton("Help");
        bHelp.addActionListener(e -> {
            try {
                URI uri = new URI("https://www.github.com/woz07/hide#Errors");
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(uri);
                }
            } catch (URISyntaxException | IOException u) {
                u.printStackTrace();
            }
        });
        
        // Finalizing components
        // p = Panel
        JPanel pTop = new JPanel(new BorderLayout());
        pTop.add(lError, BorderLayout.CENTER);
        
        JPanel pBottom = new JPanel();
        pBottom.add(bAccept);
        pBottom.add(bHelp);
        
        setLayout(new BorderLayout());
        add(pTop, BorderLayout.CENTER);
        add(pBottom, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }
}