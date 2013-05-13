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
        cw1.setAlive(true);
        cw1.setAliveForSnapshot(true);
        cw2.setAlive(true);
        cw2.setAliveForSnapshot(true);
        cw3.setAlive(true);
        cw3.setAliveForSnapshot(true);
        cw4.setAlive(true);
        cw4.setAliveForSnapshot(true);
        cw1.setNv(cw2);
        //cw1.setV(cw4);
        cw2.setSe(cw1);
        cw2.setSv(cw4);
        cw2.setNv(cw3);
        cw3.setSe(cw2);
        //cw4.setE(cw1);
        cw4.setNe(cw2);
        universe.addCellWorld(cw1);
        universe.addCellWorld(cw2);
        universe.addCellWorld(cw3);
        universe.addCellWorld(cw4);
    }

    public void ignite(){
        for (CellWorld cellWorld: universe.getUniverse()){
            System.out.print(cellWorld.isAlive() + " ");
        }
        System.out.println();
        System.out.println("---");
        for (int i=0; i<3; i++){
            for (CellWorld cellWorld: universe.getUniverse()){
                DeathByLonelinessLaw deathByLonelinessLaw = new DeathByLonelinessLaw();
                DeathByCrowdnessLaw deathByCrowdnessLaw = new DeathByCrowdnessLaw();
                cellWorld.setAliveForSnapshot(deathByLonelinessLaw.apply(cellWorld));
                //cellWorld.setAliveForSnapshot(deathByCrowdnessLaw.apply(cellWorld));
                System.out.print(cellWorld.isAliveForSnapshot() + " ");
            }
            System.out.println();
            System.out.println("---");
            for (CellWorld cellWorld: universe.getUniverse()){
                cellWorld.setAlive(cellWorld.isAliveForSnapshot());

            }
        }
    }
}
