package com.toros.gui;
import com.toros.config.*;
import javax.swing.*;
import java.awt.*;

public class Window implements Runnable {

    JFrame frame;
    Field field;
    Toolbar toolbar;

    @Override
    public void run() {

        initFrame();
        initField();
        initToolbar();
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private void initFrame() {

        frame = new JFrame();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        frame.setTitle("Conway`s Game of Life");
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void initField(){
        field = new Field(new GridLayout(Config.HORIZONTAL_BOXES, Config.VERTICAL_BOXES, 0, 0));
        frame.add(field);
    }

    private void initToolbar(){
        toolbar = new Toolbar(field);
        frame.add(toolbar);
    }
}
