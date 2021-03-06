package edu.coursework.view.frame;

import edu.coursework.controller.MainController;
import edu.coursework.view.panels.controls.ControlsPanel;
import edu.coursework.view.panels.map.MainMapPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

//class that describes main window
public class MainFrame extends JFrame {

    private MainController controller;
    private MainMapPanel mainMapPanel;
    private ControlsPanel controlsPanel;

    public MainFrame() {
        setSize(1200, 780);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Coursework project, version 1.0.0, (c) By Vilkhovyk Aleksandr and Shypilova Anna");
        setLayout(new FlowLayout());
        //create black border with blue color
        Border border = new LineBorder(Color.BLUE, 1);

        //add child panels
        controller = new MainController();

        controlsPanel = new ControlsPanel(350, 700, border, controller);
        mainMapPanel = new MainMapPanel(700, 716, border);

        controller.attachViews(mainMapPanel, controlsPanel);
        add(mainMapPanel);
        add(controlsPanel);

    }
}