package com.toros.gui;
import com.toros.config.*;
import com.toros.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import javax.swing.Box;

class Toolbar extends JPanel {

    Field field;
    JButton startButton;
    JButton stopButton;
    JButton clearButton;
    JButton saveButton;
    JLabel clickTypeLabel;
    Legend legend;
    ButtonGroup radios;
    JRadioButton oneCellClick;
    JRadioButton neighboursClick;
    TimerPanel timerPanel;

    Toolbar(Field field){
        super();
        this.field = field;

        setPreferredSize(new Dimension(Config.TOOLBAR_WIDTH, Config.FRAME_HEIGHT));
        setBackground(Config.TOOLBAR_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        startButton = Toolbar.ButtonFactory("Start");
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                field.getTimer().start();
                timerPanel.getTimer().start();
            }
        });

        stopButton = Toolbar.ButtonFactory("Stop");
        stopButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                field.getTimer().stop();
                timerPanel.getTimer().stop();
            }
        });

        clearButton = Toolbar.ButtonFactory("Clear");
        clearButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                field.clear();
                timerPanel.clear();
            }
        });

        saveButton = Toolbar.ButtonFactory("Save");
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("C:\\alikbrat\\AA_UNIVER\\OOP\\KURSACH\\conways-game\\src\\com.toros.config.saves\\field.dat"))))
                {
                    oos.writeObject(field);
                }
                catch(Exception ex){

                    ex.printStackTrace();
                }
            }
        });

        clickTypeLabel = Toolbar.LabelFactory("Choose the click type: ");

        radios = new ButtonGroup();
        oneCellClick = Toolbar.RadioFactory("One cell");
        oneCellClick.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Cell.clickMode = Cell.ClickMode.ONECELL;
            }
        });

        neighboursClick = Toolbar.RadioFactory("Neighbours");
        neighboursClick.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Cell.clickMode = Cell.ClickMode.NEIGHBOURS;
            }
        });

        Legend legend = new Legend();
        timerPanel = new TimerPanel();


        oneCellClick.setSelected(true);
        radios.add(oneCellClick);
        radios.add(neighboursClick);

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        stopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        clickTypeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        oneCellClick.setAlignmentX(Component.CENTER_ALIGNMENT);
        neighboursClick.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createRigidArea(new Dimension(0, 20)));
        add(startButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(stopButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(clearButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(saveButton);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(clickTypeLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(oneCellClick);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(neighboursClick);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(legend);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(timerPanel);
    }

    public static JButton ButtonFactory(String text){
        JButton button = new JButton(text);
        button.setBackground(Color.BLACK);
        button.setFont(new Font("Arial", Font.PLAIN, 24));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 3),
                BorderFactory.createLineBorder(Color.BLACK, 10)));

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

    public static JRadioButton RadioFactory(String text){

        JRadioButton radio = new JRadioButton(text);
        radio.setBackground(Config.TOOLBAR_COLOR);
        radio.setForeground(Color.WHITE);
        radio.setFocusPainted(false);
        radio.setFont(new Font("Arial", Font.PLAIN, 18));
        return radio;
    }

    public static JLabel LabelFactory(String text){

        JLabel label = new JLabel(text);
        label.setBackground(Config.TOOLBAR_COLOR);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        return label;
    }

    private class TimerPanel extends JPanel{

        private Timer timer;
        private JLabel timerDisplay;

        private int sec = 0;
        private int min = 0;
        private int hours = 0;

        TimerPanel(){
            super();
            setBackground(Config.TOOLBAR_COLOR);
            timerDisplay = new JLabel("00:00:00");
            timerDisplay.setBackground(Config.TOOLBAR_COLOR);
            timerDisplay.setForeground(Config.TEXT_COLOR);
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

        Timer getTimer(){
            return timer;
        }

        void clear(){
            min = 0;
            sec = 0;
            hours = 0;
            timerDisplay.setText("00:00:00");
        }
    }

    private class Legend extends JPanel{

        JLabel title;
        JPanel none;
        JPanel born;
        JPanel live;
        JPanel died;

        Legend(){
            super();
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setBackground(Config.TOOLBAR_COLOR);
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            setPreferredSize(new Dimension(Config.TOOLBAR_WIDTH, Config.FRAME_HEIGHT / 2));
            title = new JLabel("Legend: ");
            title.setForeground(Config.TEXT_COLOR);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel nonecolor = new JPanel();
            nonecolor.setBackground(Config.getColor(Status.NONE));
            nonecolor.setSize(5, 5);
            none = createLegendRow(nonecolor, new JLabel(" - dead"));

            JPanel borncolor = new JPanel();
            borncolor.setBackground(Config.getColor(Status.BORN));
            borncolor.setSize(5, 5);
            born = createLegendRow(borncolor, new JLabel(" - born"));

            JPanel livecolor = new JPanel();
            livecolor.setBackground(Config.getColor(Status.LIVE));
            livecolor.setSize(5, 5);
            live = createLegendRow(livecolor, new JLabel(" - live"));

            JPanel diedcolor = new JPanel();
            diedcolor.setBackground(Config.getColor(Status.DIED));
            diedcolor.setSize(5, 5);
            died = createLegendRow(diedcolor, new JLabel(" - dying"));

            add(title);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(none);
            add(born);
            add(live);
            add(died);
        }
    }

    private JPanel createLegendRow(Component key, Component value){
        value.setForeground(Config.TEXT_COLOR);
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
