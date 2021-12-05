package com.toros.gui;
import com.toros.config.*;
import com.toros.core.Cell;
import com.toros.core.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
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

    Toolbar(Field field, JFrame frame){
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
                File save = new File(Config.SAVES_FILE_PATH);
                try{
                    save.createNewFile();
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(save, false)))
                {
                    com.toros.gui.Box[][] boxes = field.getBoxes();
                    for(int i = 0; i < Config.VERTICAL_BOXES; i++){
                        for(int j = 0; j < Config.HORIZONTAL_BOXES; j++){
                            oos.writeObject(boxes[i][j].getCell());
                        }
                    }

                    JOptionPane.showMessageDialog(frame, "Successfully saved!", "Success", JOptionPane.PLAIN_MESSAGE);
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(frame, "Sorry, something get wrong.", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        loadSaveButton = ButtonFactory("Load Save");
        loadSaveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                File save = new File(Config.SAVES_FILE_PATH);
                if(!save.isFile()){
                    JOptionPane.showMessageDialog(frame, "Error! No save found.", "No save", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(save)))
                {
                    com.toros.gui.Box[][] boxes = field.getBoxes();
                    for(int i = 0; i < Config.VERTICAL_BOXES; i++){
                        for(int j = 0; j < Config.HORIZONTAL_BOXES; j++){
                            boxes[i][j].setCell((Cell) ois.readObject());
                        }
                    }

                    //Define neighbours
                    for (int x = 0; x < Config.VERTICAL_BOXES; x++) {
                        for (int y = 0; y < Config.HORIZONTAL_BOXES; y++) {
                            for(int sx = -1; sx <= 1; sx++){
                                for(int sy = -1; sy <= 1; sy++){
                                    if(sx == 0 && sy == 0) continue;
                                    //Compute neighbours to create torus-like field
                                    boxes[x][y].getCell().addNeighbour(boxes
                                            [(x + sx + Config.VERTICAL_BOXES) % Config.VERTICAL_BOXES]
                                            [(y + sy + Config.HORIZONTAL_BOXES) % Config.HORIZONTAL_BOXES].getCell());
                                }
                            }
                        }
                    }

                    JOptionPane.showMessageDialog(frame, "Successfully loaded!", "Success", JOptionPane.PLAIN_MESSAGE);
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(frame, "Sorry, something get wrong.", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        randomConfButton = ButtonFactory("Random");
        randomConfButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                com.toros.gui.Box[][] boxes = field.getBoxes();
                for(int i = 0; i < Config.VERTICAL_BOXES; i++){
                    for(int j = 0; j < Config.HORIZONTAL_BOXES; j++){
                        if(Math.random() < Config.LIVE_CELL_CHANCE) {
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
                new SpinnerNumberModel(Config.SLEEPMS, 1, 100000, 10);
        speedFieldSpinner = new JSpinner(speedModel);
        speedFieldSpinner.setEditor(new JSpinner.DefaultEditor(speedFieldSpinner));
        speedFieldSpinner.setMaximumSize(new Dimension(100, 20));
        speedFieldSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Config.SLEEPMS = (int)speedFieldSpinner.getValue() / 2;
                field.getTimer().setDelay(Config.SLEEPMS);
            }
        });

        JLabel chanceSpinnerLabel = LabelFactory("Live cell chance(%): ");
        chanceSpinnerLabel.setAlignmentX(CENTER_ALIGNMENT);
        SpinnerModel chanceModel =
                new SpinnerNumberModel(50, 1, 100, 1);
        initialChanceSpinner = new JSpinner(chanceModel);
        initialChanceSpinner.setEditor(new JSpinner.DefaultEditor(initialChanceSpinner));
        initialChanceSpinner.setMaximumSize(new Dimension(100, 20));
        initialChanceSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
               int chance = (int)initialChanceSpinner.getValue();
               Config.LIVE_CELL_CHANCE = (double)chance / 100.0;
            }
        });


        /* LEGEND */
        Legend legend = new Legend();

        /* TIMER PANEL */
        timerPanel = new TimerPanel();

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

    public static JButton ButtonFactory(String text){
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

    public static JLabel LabelFactory(String text){

        JLabel label = new JLabel(text);
        label.setBackground(Config.TOOLBAR_COLOR);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    void repaintField(){
        com.toros.gui.Box[][] boxes = field.getBoxes();
        for(int i = 0; i < Config.VERTICAL_BOXES; i++){
            for(int j = 0; j < Config.HORIZONTAL_BOXES; j++){
                boxes[i][j].setColor();
            }
        }
    }

    private class TimerPanel extends JPanel{

        private Timer timer;
        private JLabel timerDisplay;

        private int sec = 0;
        private int min = 0;
        private int hours = 0;

        private TimerPanel(){
            super();
            setBackground(Config.TOOLBAR_COLOR);
            timerDisplay = LabelFactory("00:00:00");
            timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(sec + 1 == 60){
                        min++;
                        sec = -1;
                    }
                    if(min + 1 == 60){
                        hours++;
                        min = 0;
                    }
                    sec++;

                    String hh = hours < 10 ? "0" + hours : "" + hours;
                    String mm = min < 10 ? "0" + min : "" + min;
                    String ss = sec < 10 ? "0" + sec : "" + sec;
                    timerDisplay.setText(hh+ ":" + mm + ":" + ss);
                }
            });

            add(timerDisplay);
        }

        private void clear(){
            min = 0;
            sec = 0;
            hours = 0;
            timerDisplay.setText("00:00:00");
        }
    }

    private class Legend extends JPanel{

        private JLabel title;

        Legend(){
            super();
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setBackground(Config.TOOLBAR_COLOR);
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            setPreferredSize(new Dimension(Config.TOOLBAR_WIDTH, Config.FRAME_HEIGHT / 2));
            title = LabelFactory("Legend(click on colorbox to select): ");
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(title);
            add(Box.createRigidArea(new Dimension(0, 10)));

            for(Status status: Status.values()){
                JPanel colorbox = new JPanel();
                colorbox.setBackground(Config.getColor(status));
                colorbox.setSize(10, 10);
                colorbox.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Color color = JColorChooser.showDialog(frame, "Choose " + status.toString() + " color", Config.getColor(status));
                        if(color != null){
                            switch(status){
                                case NONE -> Config.NONE_COLOR = color;
                                case BORN -> Config.BORN_COLOR = color;
                                case LIVE -> Config.LIVE_COLOR = color;
                                case DIED -> Config.DIED_COLOR = color;
                            }
                            repaintField();
                            colorbox.setBackground(color);
                        }
                    }
                });
                add(createLegendRow(colorbox, LabelFactory(" - " + status.toString())));
            }
        }
    }

    private JPanel createLegendRow(Component key, Component value){
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
