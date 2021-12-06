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

        /* GUI INITIALIZATION */
        fetchConfig();
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
        frame.setTitle("Conway`s Game of Life by Arzuman");
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void initField() {
        field = new Field(new GridLayout(Config.HORIZONTAL_BOXES, Config.VERTICAL_BOXES, 0, 0));
        frame.add(field);
    }

    private void initToolbar() {
        toolbar = new Toolbar(field, frame);
        frame.add(toolbar);
    }

    private void fetchConfig() {

        /* CHECK FOR THE CONFIG SAVES */
        Config.SLEEPMS = Config.userPref.getInt("SLEEPMS", Config.SLEEPMS);
        Config.LIVE_CELL_CHANCE = Config.userPref.getDouble("LIVE_CELL_CHANCE", Config.LIVE_CELL_CHANCE);
        Config.NONE_COLOR = new Color(Config.userPref.getInt("NONE_COLOR", Config.NONE_COLOR.getRGB()));
        Config.BORN_COLOR = new Color(Config.userPref.getInt("BORN_COLOR", Config.BORN_COLOR.getRGB()));
        Config.LIVE_COLOR = new Color(Config.userPref.getInt("LIVE_COLOR", Config.LIVE_COLOR.getRGB()));
        Config.DIED_COLOR = new Color(Config.userPref.getInt("DIED_COLOR", Config.DIED_COLOR.getRGB()));
    }
}
