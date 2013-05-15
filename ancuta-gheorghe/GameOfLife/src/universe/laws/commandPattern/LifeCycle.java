package universe.laws.commandPattern;

import universe.CellWorld;

/**
 * Created with IntelliJ IDEA.
 * User: Ancuta Gheorghe
 * Date: 5/14/13
 * Time: 3:34 PM
 * To change this template use File | Settings | File Templates.
 */

//receiver class
public class LifeCycle {

    private CellWorld cellWorld;

    public LifeCycle(CellWorld cellWorld) {
        this.cellWorld = cellWorld;
    }

    public void die(){
        this.cellWorld.setAlive(false);
    }

    public void live(){
        this.cellWorld.setAlive(true);
    }
}
