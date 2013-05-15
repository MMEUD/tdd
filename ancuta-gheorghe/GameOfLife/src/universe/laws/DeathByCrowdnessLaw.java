package universe.laws;

import universe.CellWorld;

/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/11/13
 * Time: 1:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class DeathByCrowdnessLaw implements ILaw {
    @Override
    public boolean apply(CellWorld cellWorld) {
        if (cellWorld.getNumberOfNeighbors() > 3) {
           return false;
        }
        return true;
    }
}
