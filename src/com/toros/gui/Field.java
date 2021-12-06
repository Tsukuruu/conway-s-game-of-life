package com.toros.gui;

import com.toros.config.*;
import com.toros.core.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Field extends JPanel {

    private Box[][] boxes;
    private Timer timer;

    Field(GridLayout layout) {
        super();

        setSize(Config.FIELD_WIDTH, Config.FIELD_HEIGHT);
        setLayout(layout);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, Color.RED));

        initBoxes();
        initTimer();

        addMouseListener(new FieldClicker());
        addMouseMotionListener(new FieldDragger());
    }

    public Box[][] getBoxes() {
        return boxes;
    }

    public Timer getTimer() {
        return timer;
    }

    private void initBoxes() {

        boxes = new Box[Config.VERTICAL_BOXES][Config.HORIZONTAL_BOXES];
        for (int x = 0; x < Config.VERTICAL_BOXES; x++) {
            for (int y = 0; y < Config.HORIZONTAL_BOXES; y++) {
                boxes[x][y] = new Box(x, y);
                this.add(boxes[x][y]);
            }
        }

        //Define neighbours
        for (int x = 0; x < Config.VERTICAL_BOXES; x++) {
            for (int y = 0; y < Config.HORIZONTAL_BOXES; y++) {
                for (int sx = -1; sx <= 1; sx++) {
                    for (int sy = -1; sy <= 1; sy++) {
                        if (sx == 0 && sy == 0) continue;
                        //Compute neighbours to create torus-like field
                        boxes[x][y].getCell().addNeighbour(boxes
                                [(x + sx + Config.VERTICAL_BOXES) % Config.VERTICAL_BOXES]
                                [(y + sy + Config.HORIZONTAL_BOXES) % Config.HORIZONTAL_BOXES].getCell());
                    }
                }
            }
        }
    }

    private void initTimer() {
        TimerListener tl = new TimerListener();
        timer = new Timer(Config.SLEEPMS, tl);
    }


    void clear() {
        for (int x = 0; x < Config.VERTICAL_BOXES; x++) {
            for (int y = 0; y < Config.HORIZONTAL_BOXES; y++) {
                boxes[x][y].getCell().setStatus(Status.NONE);
                boxes[x][y].setColor();
            }
        }
    }

    private class TimerListener implements ActionListener {
        boolean flop = false;

        @Override
        public void actionPerformed(ActionEvent e) {
            flop = !flop;
            for (int x = 0; x < Config.VERTICAL_BOXES; x++) {
                for (int y = 0; y < Config.HORIZONTAL_BOXES; y++) {
                    if (flop) {
                        boxes[x][y].prepare();
                    } else {
                        boxes[x][y].change();
                    }
                }
            }
        }
    }

    private class FieldClicker extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            Point point = e.getPoint();
            int xbox = point.y / Config.BOX_SIZE;
            int ybox = point.x / Config.BOX_SIZE;
            if ((xbox < Config.VERTICAL_BOXES && xbox > 0) && (ybox < Config.HORIZONTAL_BOXES && ybox > 0)) {
                boxes[xbox][ybox].turn();
            }
        }
    }

    private class FieldDragger extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            Point point = e.getPoint();
            int xbox = point.y / Config.BOX_SIZE;
            int ybox = point.x / Config.BOX_SIZE;
            if ((xbox < Config.VERTICAL_BOXES && xbox > 0) && (ybox < Config.HORIZONTAL_BOXES && ybox > 0)) {
                boxes[xbox][ybox].turn();
            }
        }
    }
}
