package universe;

import universe.CellWorld;
import universe.Universe;

/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/10/13
 * Time: 11:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class BigBang {

    Universe universe = new Universe();

    public BigBang(){
        universe.addCellWorld(new CellWorld(new XAxis(1), new YAxis(1)));
    }

    public void ignite(){
        for (CellWorld cellWorld: universe.getUniverse()){
            System.out.print(cellWorld.isAlive() + " ");
        }
        System.out.println();
        System.out.println("---");
        for (int i=0; i<3; i++){
            UniverseSnapshot universeSnapshot = new UniverseSnapshot();
            universeSnapshot.setUniverse(universe.getUniverse());
            for (CellWorld cellWorld: universeSnapshot.getUniverse()){
                DeathByLonelinessLaw deathByLonelinessLaw = new DeathByLonelinessLaw();
                //DeathByCrowdnessLaw deathByCrowdnessLaw = new DeathByCrowdnessLaw();
                //BirthLaw birthLaw = new BirthLaw();
                deathByLonelinessLaw.apply(cellWorld);
                //deathByCrowdnessLaw.apply(cellWorld);
                //birthLaw.apply(cellWorld);
                System.out.print(cellWorld.isAlive() + " ");
            }
            System.out.println();
            System.out.println("---");
        }
    }
}
