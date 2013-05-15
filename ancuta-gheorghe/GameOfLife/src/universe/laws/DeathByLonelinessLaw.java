package universe.laws;

import universe.CellWorld;

/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/11/13
 * Time: 9:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class DeathByLonelinessLaw implements ILaw {
    @Override
    public boolean apply(CellWorld cellWorld) {
        if (cellWorld.getNumberOfNeighbors() < 2) {
           return false;
        }
        return true;
    }
}
