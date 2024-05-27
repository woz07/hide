package github.woz07.hide;

import github.woz07.BCipher;

import javax.swing.*;

public class Application extends JFrame {
    private final BCipher bCipher;
    public Application() {
        bCipher = new BCipher();
        
    }
    
    public static void main(String[] args) {
        new Application();
    }
}
