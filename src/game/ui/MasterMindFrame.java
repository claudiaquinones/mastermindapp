package game.ui;

import javax.swing.*;
import java.awt.*;

public class MasterMindFrame extends JFrame {

    public static void main(String[] args){

        JFrame frame = new JFrame("MasterMind Game");
        frame.add(new MainPanel());
        frame.setSize(1000,1000);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}






