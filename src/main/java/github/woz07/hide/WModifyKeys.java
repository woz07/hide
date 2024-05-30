package github.woz07.hide;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WModifyKeys extends JFrame {
    private final Application parent;
    // l = List
    // jl = JList
    private final List<String> lKeys;
    private final DefaultListModel<String> lModel;
    private final JList<String> jlKeys;
    
    // p = Panel
    // b = Button
    private final JScrollPane pane;
    private final JPanel pButtons;
    private final JButton bAdd;
    private final JButton bDelete;
    public WModifyKeys(Application parent) {
        this.parent = parent;
        // Setting up
        setTitle("Hide ~ Modify keys");
        setPreferredSize(new Dimension(300, 200));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Setting up components
        lKeys = new ArrayList<>();
        lModel = new DefaultListModel<>();
        
        jlKeys = new JList<>(lModel);
        jlKeys.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pane = new JScrollPane(jlKeys);
        
        // Adding
        bAdd = new JButton("Add Key");
        bAdd.addActionListener(e -> {
            String key = JOptionPane.showInputDialog(WModifyKeys.this, "Enter key:");
            if (key != null && !key.isEmpty() && valid(key)) {
                parent.kAdd(Byte.parseByte(key));
                load();
            }
        });
        
        // Deleting
        bDelete = new JButton("Delete Key");
        bDelete.addActionListener(e -> {
            int selected = jlKeys.getSelectedIndex();
            if (selected != -1) {
                parent.kRemove(selected);
                load();
            } else {
                JOptionPane.showMessageDialog(WModifyKeys.this, "Please select a key to delete.");
            }
        });
        
        pButtons = new JPanel();
        pButtons.add(bAdd);
        pButtons.add(bDelete);
        
        // Load keys into lModel
        load();
        
        // Finalize components
        setLayout(new BorderLayout());
        add(pane, BorderLayout.CENTER);
        add(pButtons, BorderLayout.SOUTH);
        
        // Finalizing
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }
    
    /**
     * Function to clear lModel and then load keys into lModel
     */
    private void load() {
        lModel.clear();
        for (byte key : parent.getKeys()) {
            if (key == 0) { return; } // This means we have hit the end where the 0's are
            lModel.addElement(String.valueOf(key));
        }
    }
    
    /**
     * Function to check if a string is a valid byte
     * @param input The string to check
     * @return True if the byte is within the range 1 to 255 else false
     */
    private boolean valid(String input) {
        try {
            byte value = Byte.parseByte(input);
            return value >= 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}