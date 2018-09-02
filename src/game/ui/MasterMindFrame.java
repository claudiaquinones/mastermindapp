package game.ui;

import javax.swing.*;
import java.awt.*;

public class MasterMindFrame extends JFrame {

    public static void main(String[] args){

        JFrame frame = new JFrame("MasterMind Game");
        frame.add(new MainPanel());
        frame.setSize(800,800);
        frame.setVisible(true);
    }
}
