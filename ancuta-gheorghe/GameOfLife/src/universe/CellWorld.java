package universe;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/11/13
 * Time: 9:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class CellWorld extends Cell{

    private ArrayList<Cell> neighbors = new ArrayList<Cell>();

    public CellWorld(int id) {
        super(id);
    }

    public CellWorld(XAxis x, YAxis y) {
        super(x, y);
    }

    public ArrayList<Cell> getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(Cell cell){
        this.neighbors.add(cell);
    }

    public void removeNeighbor(Cell cell){
        this.neighbors.remove(cell);
    }

    public void generateNeighbors(){
        //lalalala
    }
}
