package com.toros.gui;

import com.toros.config.*;
import com.toros.core.*;

import javax.swing.*;

public class Box extends JPanel {

    private Cell cell;

    Box(int x, int y) {
        super();
        cell = new Cell(Status.NONE);
        setBounds(x * Config.BOX_SIZE, y * Config.BOX_SIZE, Config.BOX_SIZE, Config.BOX_SIZE);
        setColor();
    }

    Cell getCell() {
        return this.cell;
    }

    void setCell(Cell cell) {
        this.cell = cell;
        setColor();
    }


    void setColor() {
        setBackground(Config.getColor(cell.getStatus()));
    }

    void prepare() {
        cell.prepare();
        setColor();
    }

    void change() {
        cell.change();
        setColor();
    }

    void turn() {
        cell.turn();
        setColor();
    }
}
