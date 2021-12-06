package com.toros.gui;

import com.toros.config.*;
import com.toros.core.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Box;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class Toolbar extends JPanel {

    /* Link to frame for dialog windows */
    JFrame frame;

    Field field;

    JButton startButton;
    JButton stopButton;
    JButton clearButton;
    JButton saveButton;
    JButton loadSaveButton;
    JButton randomConfButton;

    JSpinner speedFieldSpinner;
    JSpinner initialChanceSpinner;

    Legend legend;
    TimerPanel timerPanel;

    Toolbar(Field field, JFrame frame) {
        super();
        this.field = field;
        this.frame = frame;

        setPreferredSize(new Dimension(Config.TOOLBAR_WIDTH, Config.FRAME_HEIGHT));
        setBackground(Config.TOOLBAR_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        /* BUTTONS */
        startButton = ButtonFactory("Start");
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                field.getTimer().start();
                timerPanel.timer.start();
            }
        });

        stopButton = ButtonFactory("Stop");
        stopButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                field.getTimer().stop();
                timerPanel.timer.stop();
            }
        });

        clearButton = ButtonFactory("Clear");
        clearButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                field.clear();
                timerPanel.clear();
            }
        });

        saveButton = ButtonFactory("Save");
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                com.toros.gui.Box[][] boxes = field.getBoxes();
                for (int i = 0; i < Config.VERTICAL_BOXES; i++) {
                    for (int j = 0; j < Config.HORIZONTAL_BOXES; j++) {
                        int status = 0;
                        switch (boxes[i][j].getCell().getStatus()) {
                            case NONE -> status = 0;
                            case BORN -> status = 1;
                            case LIVE -> status = 2;
                            case DIED -> status = 3;
                        }
                        Config.userPref.putInt("cell" + i + "_" + j + "_status", status);
                    }
                }

                JOptionPane.showMessageDialog(frame, "Successfully saved!", "Success", JOptionPane.PLAIN_MESSAGE);
            }
        });

        loadSaveButton = ButtonFactory("Load Save");
        loadSaveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                com.toros.gui.Box[][] boxes = field.getBoxes();
                for (int i = 0; i < Config.VERTICAL_BOXES; i++) {
                    for (int j = 0; j < Config.HORIZONTAL_BOXES; j++) {
                        Status status = Status.NONE;
                        switch (Config.userPref.getInt("cell" + i + "_" + j + "_status", 0)) {
                            case 0 -> status = Status.NONE;
                            case 1 -> status = Status.BORN;
                            case 2 -> status = Status.LIVE;
                            case 3 -> status = Status.DIED;
                        }
                        boxes[i][j].getCell().setStatus(status);
                        boxes[i][j].setColor();
                    }
                }

                JOptionPane.showMessageDialog(frame, "Successfully loaded!", "Success", JOptionPane.PLAIN_MESSAGE);
            }
        });

        randomConfButton = ButtonFactory("Random");
        randomConfButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                com.toros.gui.Box[][] boxes = field.getBoxes();
                for (int i = 0; i < Config.VERTICAL_BOXES; i++) {
                    for (int j = 0; j < Config.HORIZONTAL_BOXES; j++) {
                        if (Math.random() < Config.LIVE_CELL_CHANCE) {
                            boxes[i][j].getCell().setStatus(Status.LIVE);
                            boxes[i][j].setColor();
                        }
                    }
                }
            }
        });


        /* SPINNERS */
        JLabel speedSpinnerLabel = LabelFactory("One generation tick time: ");
        speedSpinnerLabel.setAlignmentX(CENTER_ALIGNMENT);
        SpinnerModel speedModel =
                new SpinnerNumberModel(Config.SLEEPMS * 2, 1, 100000, 10);
        speedFieldSpinner = new JSpinner(speedModel);
        speedFieldSpinner.setEditor(new JSpinner.DefaultEditor(speedFieldSpinner));
        speedFieldSpinner.setMaximumSize(new Dimension(100, 20));
        speedFieldSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Config.SLEEPMS = (int) speedFieldSpinner.getValue() / 2;
                field.getTimer().setDelay(Config.SLEEPMS);
                Config.userPref.putInt("SLEEPMS", Config.SLEEPMS);
            }
        });

        JLabel chanceSpinnerLabel = LabelFactory("Live cell chance(%): ");
        chanceSpinnerLabel.setAlignmentX(CENTER_ALIGNMENT);
        SpinnerModel chanceModel =
                new SpinnerNumberModel((int) (Config.LIVE_CELL_CHANCE * 100), 1, 100, 1);
        initialChanceSpinner = new JSpinner(chanceModel);
        initialChanceSpinner.setEditor(new JSpinner.DefaultEditor(initialChanceSpinner));
        initialChanceSpinner.setMaximumSize(new Dimension(100, 20));
        initialChanceSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int chance = (int) initialChanceSpinner.getValue();
                Config.LIVE_CELL_CHANCE = (double) chance / 100.0;
                Config.userPref.putDouble("LIVE_CELL_CHANCE", Config.LIVE_CELL_CHANCE);
            }
        });


        /* LEGEND */
        legend = new Legend();

        /* TIMER PANEL */
        timerPanel = new TimerPanel();

        /* BUTTON PANEL */
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Config.TOOLBAR_COLOR);
        buttonsPanel.setPreferredSize(new Dimension(Config.TOOLBAR_WIDTH - 20, Config.FRAME_HEIGHT - 300));
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonsPanel.add(startButton);
        buttonsPanel.add(stopButton);
        buttonsPanel.add(clearButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(loadSaveButton);
        buttonsPanel.add(randomConfButton);

        /* ADDING COMPONENTS */
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(buttonsPanel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(chanceSpinnerLabel);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(initialChanceSpinner);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(speedSpinnerLabel);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(speedFieldSpinner);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(legend);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(timerPanel);
    }

    public static JButton ButtonFactory(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.BLACK);
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 3),
                BorderFactory.createLineBorder(Color.BLACK, 10)));


        /* Add hover effect */
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(Color.RED);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.RED, 3),
                        BorderFactory.createLineBorder(Color.BLACK, 10)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 3),
                        BorderFactory.createLineBorder(Color.BLACK, 10)));
            }
        });

        return button;
    }

    public static JLabel LabelFactory(String text) {

        JLabel label = new JLabel(text);
        label.setBackground(Config.TOOLBAR_COLOR);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    private class TimerPanel extends JPanel {

        private Timer timer;
        private JLabel timerDisplay;

        private int sec = 0;
        private int min = 0;
        private int hours = 0;

        private TimerPanel() {
            super();
            setBackground(Config.TOOLBAR_COLOR);
            timerDisplay = LabelFactory("00:00:00");
            timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (sec + 1 == 60) {
                        min++;
                        sec = -1;
                    }
                    if (min + 1 == 60) {
                        hours++;
                        min = 0;
                    }
                    sec++;

                    String hh = hours < 10 ? "0" + hours : "" + hours;
                    String mm = min < 10 ? "0" + min : "" + min;
                    String ss = sec < 10 ? "0" + sec : "" + sec;
                    timerDisplay.setText(hh + ":" + mm + ":" + ss);
                }
            });

            add(timerDisplay);
        }

        private void clear() {
            min = 0;
            sec = 0;
            hours = 0;
            timerDisplay.setText("00:00:00");
        }
    }

    private class Legend extends JPanel {

        private JLabel title;

        Legend() {
            super();
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setBackground(Config.TOOLBAR_COLOR);
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            setPreferredSize(new Dimension(Config.TOOLBAR_WIDTH, Config.FRAME_HEIGHT / 2));
            title = LabelFactory("Legend(click on colorbox to select): ");
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(title);
            add(Box.createRigidArea(new Dimension(0, 10)));

            for (Status status : Status.values()) {
                JPanel colorbox = new JPanel();
                colorbox.setBackground(Config.getColor(status));
                colorbox.setSize(10, 10);
                colorbox.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Color color = JColorChooser.showDialog(frame, "Choose " + status.toString() + " color", Config.getColor(status));
                        if (color != null) {
                            switch (status) {
                                case NONE -> Config.NONE_COLOR = color;
                                case BORN -> Config.BORN_COLOR = color;
                                case LIVE -> Config.LIVE_COLOR = color;
                                case DIED -> Config.DIED_COLOR = color;
                            }
                            repaintField();
                            colorbox.setBackground(color);
                            Config.userPref.putInt(status.toString() + "_COLOR", color.getRGB());
                        }
                    }
                });
                add(createLegendRow(colorbox, LabelFactory(" - " + status.toString())));
            }
        }

        private void repaintField() {
            com.toros.gui.Box[][] boxes = field.getBoxes();
            for (int i = 0; i < Config.VERTICAL_BOXES; i++) {
                for (int j = 0; j < Config.HORIZONTAL_BOXES; j++) {
                    boxes[i][j].setColor();
                }
            }
        }

        private JPanel createLegendRow(Component key, Component value) {
            JPanel row = new JPanel();
            row.setAlignmentX(Component.CENTER_ALIGNMENT);
            row.setBackground(Config.TOOLBAR_COLOR);
            row.setLayout(new FlowLayout());
            row.add(key);
            row.add(value);
            row.setMaximumSize(new Dimension(100, 30));
            return row;
        }
    }
}
