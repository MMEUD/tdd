package universe;

/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/11/13
 * Time: 9:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class BirthLaw implements ILaw {
    @Override
    public boolean apply(CellWorld cellWorld) {
        if (cellWorld.getNumberOfNeighbors() == 3) {
            return true;
        }
        return false;
    }
}
