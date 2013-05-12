package universe;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/11/13
 * Time: 12:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class Universe {

    private ArrayList<CellWorld> universe;

    public Universe(){
        this.universe = new ArrayList<CellWorld>();
    }

    public void addCellWorld(CellWorld cellWorld){
        this.universe.add(cellWorld);
    }

    public ArrayList<CellWorld> getUniverse() {
        return universe;
    }

    public void setUniverse(ArrayList<CellWorld> universe) {
        this.universe = universe;
    }
}
