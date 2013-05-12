package universe;

/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/11/13
 * Time: 1:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class DeathByCrowdnessLaw implements ILaw{
    @Override
    public void apply(CellWorld cellWorld) {
        int aliveNeighbors = 0;
        for (int i=0; i<cellWorld.getNeighbors().size(); i++){
            if (cellWorld.getNeighbors().get(i).isAlive()){
                aliveNeighbors++;
            }
        }
        if (aliveNeighbors > 3){
            cellWorld.setAlive(false);
        }
    }
}
