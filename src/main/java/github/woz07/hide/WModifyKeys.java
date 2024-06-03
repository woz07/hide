package github.woz07.hide;

import javax.swing.*;
import java.awt.*;

public class WModifyKeys extends JFrame {
    private final Application parent;
    private final DefaultListModel<String> lModel;
    private final JList<String> jlKeys;
    private final JScrollPane pane;
    private final JPanel pButtons;
    private final JButton bAdd;
    private final JButton bDelete;
    private final JButton bClear;
    
    public WModifyKeys(Application parent) {
        this.parent = parent;
        setTitle("Hide ~ Modify keys");
        setPreferredSize(new Dimension(320, 200));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        lModel = new DefaultListModel<>();
        jlKeys = new JList<>(lModel);
        jlKeys.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pane = new JScrollPane(jlKeys);
        
        // Adding
        bAdd = new JButton("Add Key");
        bAdd.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(WModifyKeys.this, "Enter key (or n key to add multiple):");
            if (input != null && !input.isEmpty()) {
                String[] parts = input.split(" ");
                if (parts.length == 2 && valid(parts[1])) {
                    try {
                        int times = Integer.parseInt(parts[0]);
                        byte key = Byte.parseByte(parts[1]);
                        if (times + parent.getKeys().size() > 255) {
                            JOptionPane.showMessageDialog(WModifyKeys.this, "Cannot add more than 255 keys in total.");
                            return;
                        }
                        for (int i = 0; i < times; i++) {
                            parent.kAdd(key);
                        }
                        load();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(WModifyKeys.this, "Invalid format. Use 'n key' to add multiple keys.");
                    }
                } else if (valid(input)) {
                    if (parent.getKeys().size() >= 255) {
                        JOptionPane.showMessageDialog(WModifyKeys.this, "Cannot add more than 255 keys in total.");
                        return;
                    }
                    parent.kAdd(Byte.parseByte(input));
                    load();
                } else {
                    JOptionPane.showMessageDialog(WModifyKeys.this, "Invalid key.");
                }
            }
        });
        
        // Deleting
        bDelete = new JButton("Delete Key");
        bDelete.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(WModifyKeys.this, "Enter key (or n key to delete multiple):");
            if (input != null && !input.isEmpty()) {
                String[] parts = input.split(" ");
                if (parts.length == 2 && valid(parts[1])) {
                    try {
                        if (parts[0].equals("*")) {
                            byte key = Byte.parseByte(parts[1]);
                            int index;
                            while ((index = findKeyIndex(key)) != -1) {
                                parent.kRemove(index);
                            }
                        } else {
                            int times = Integer.parseInt(parts[0]);
                            byte key = Byte.parseByte(parts[1]);
                            for (int i = 0; i < times; i++) {
                                int index = findKeyIndex(key);
                                if (index != -1) {
                                    parent.kRemove(index);
                                } else {
                                    break;
                                }
                            }
                        }
                        load();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(WModifyKeys.this, "Invalid format. Use 'n key' or '* key' to delete multiple keys.");
                    }
                } else if (valid(input)) {
                    int index = findKeyIndex(Byte.parseByte(input));
                    if (index != -1) {
                        parent.kRemove(index);
                        load();
                    } else {
                        JOptionPane.showMessageDialog(WModifyKeys.this, "Key not found.");
                    }
                } else {
                    JOptionPane.showMessageDialog(WModifyKeys.this, "Invalid key.");
                }
            }
        });
        
        // Clearing
        bClear = new JButton("Clear key(s)");
        bClear.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(WModifyKeys.this, "Confirm clearing of key(s)?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                parent.kClear();
                lModel.clear();
            }
        });
        
        pButtons = new JPanel();
        pButtons.add(bAdd);
        pButtons.add(bDelete);
        pButtons.add(bClear);
        
        // Load keys into lModel
        load();
        
        setLayout(new BorderLayout());
        add(pane, BorderLayout.CENTER);
        add(pButtons, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }
    
    private void load() {
        lModel.clear();
        for (int i = 0; i < parent.getKeys().size(); i++) {
            byte key = parent.getKeys().get(i);
            lModel.addElement((i + 1) + ": " + key);  // Index starting from 1
        }
    }
    
    private boolean valid(String input) {
        try {
            byte value = Byte.parseByte(input);
            return value >= 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private int findKeyIndex(byte key) {
        for (int i = 0; i < parent.getKeys().size(); i++) {
            if (parent.getKeys().get(i) == key) {
                return i;
            }
        }
        return -1;
    }
}
