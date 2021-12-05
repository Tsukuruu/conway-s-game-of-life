package com.toros.core;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Cell implements Serializable{

    private transient ArrayList<Cell> neighbours;
    private transient Status status;

    public Cell(Status status){
        this.status = status;
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

    private void writeObject(ObjectOutputStream oos)
            throws IOException {
        oos.defaultWriteObject();
        int status = 0;
        switch (this.status){
            case NONE:
                status = 0;
                break;
            case BORN:
                status = 1;
                break;
            case LIVE:
                status = 2;
                break;
            case DIED:
                status = 3;
                break;
        }
        oos.writeObject(status);
    }

    private void readObject(ObjectInputStream ois)
            throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        Integer houseNumber = (Integer) ois.readObject();
        switch (houseNumber){
            case 0:
                this.status = Status.NONE;
                break;
            case 1:
                this.status = Status.BORN;
                break;
            case 2:
                this.status = Status.LIVE;
                break;
            case 3:
                this.status = Status.DIED;
                break;
            default:
                this.status = Status.NONE;
        }

        this.neighbours = new ArrayList<>();
    }
}
