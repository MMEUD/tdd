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
        CellWorld cw1 = new CellWorld(1);
        CellWorld cw2 = new CellWorld(2);
        CellWorld cw3 = new CellWorld(3);
        CellWorld cw4 = new CellWorld(4);
        CellWorld cw5 = new CellWorld(5);
        CellWorld cw6 = new CellWorld(6);
        CellWorld cw7 = new CellWorld(7);
        CellWorld cw8 = new CellWorld(8);
        CellWorld cw9 = new CellWorld(9);

        cw2.setAlive(true);
        cw3.setAlive(true);
        cw4.setAlive(true);
        cw6.setAlive(true);
        cw7.setAlive(true);
        cw9.setAlive(true);

        cw1.addNeighbor(cw2);
        cw1.addNeighbor(cw4);
        cw1.addNeighbor(cw5);

        cw2.addNeighbor(cw1);
        cw2.addNeighbor(cw3);
        cw2.addNeighbor(cw4);
        cw2.addNeighbor(cw5);
        cw2.addNeighbor(cw6);

        cw3.addNeighbor(cw2);
        cw3.addNeighbor(cw5);
        cw3.addNeighbor(cw6);

        cw4.addNeighbor(cw1);
        cw4.addNeighbor(cw2);
        cw4.addNeighbor(cw5);
        cw4.addNeighbor(cw7);
        cw4.addNeighbor(cw8);

        cw5.addNeighbor(cw1);
        cw5.addNeighbor(cw2);
        cw5.addNeighbor(cw3);
        cw5.addNeighbor(cw4);
        cw5.addNeighbor(cw6);
        cw5.addNeighbor(cw7);
        cw5.addNeighbor(cw8);
        cw5.addNeighbor(cw9);

        cw6.addNeighbor(cw2);
        cw6.addNeighbor(cw3);
        cw6.addNeighbor(cw5);
        cw6.addNeighbor(cw8);
        cw6.addNeighbor(cw9);

        cw7.addNeighbor(cw4);
        cw7.addNeighbor(cw5);
        cw7.addNeighbor(cw8);

        cw8.addNeighbor(cw4);
        cw8.addNeighbor(cw5);
        cw8.addNeighbor(cw6);
        cw8.addNeighbor(cw7);
        cw8.addNeighbor(cw9);

        cw9.addNeighbor(cw5);
        cw9.addNeighbor(cw6);
        cw9.addNeighbor(cw8);


        /*
        cw1.addNeighbor(cw2);
        cw1.addNeighbor(cw3);
        cw1.addNeighbor(cw4);

        cw2.addNeighbor(cw1);
        cw2.addNeighbor(cw3);
        cw2.addNeighbor(cw4);

        cw3.addNeighbor(cw1);
        cw3.addNeighbor(cw2);
        cw3.addNeighbor(cw4);

        cw4.addNeighbor(cw1);
        cw4.addNeighbor(cw2);
        cw4.addNeighbor(cw3);
        */
        universe.addCellWorld(cw1);
        universe.addCellWorld(cw2);
        universe.addCellWorld(cw3);
        universe.addCellWorld(cw4);
        universe.addCellWorld(cw5);
        universe.addCellWorld(cw6);
        universe.addCellWorld(cw7);
        universe.addCellWorld(cw8);
        universe.addCellWorld(cw9);
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
