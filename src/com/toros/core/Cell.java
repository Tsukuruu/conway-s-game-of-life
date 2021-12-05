package com.toros.core;
import java.io.Serializable;
import java.util.ArrayList;

public class Cell implements Serializable{

    private ArrayList<Cell> neighbours;
    private Status status;

    public Cell(){
        status = Status.NONE;
        neighbours = new ArrayList<>();
    }

    public Status getStatus(){
        return this.status;
    }

    public void setStatus(Status status){ this.status = status; }

    public void addNeighbour(Cell cell){
        neighbours.add(cell);
    }

    public void prepare(){
        int around = countNeighbourCells();
        status = status.prepare(around);
    }

    public void change(){
        status = status.change();
    }

    public void turn(){
        this.status = this.status.cellExists() ? Status.NONE: Status.LIVE;
    }

    private int countNeighbourCells(){
        int count = 0;
        for(Cell cell : neighbours){
            if(cell.status.cellExists()){
                count++;
            }
        }
        return count;
    }
}
