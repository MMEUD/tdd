package universe;

/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/11/13
 * Time: 1:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class Cell {

    private int id;
    private boolean isAlive = false;
    private boolean isAliveForSnapshot = false;

    public Cell(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isAliveForSnapshot() {
        return isAliveForSnapshot;
    }

    public void setAliveForSnapshot(boolean aliveForSnapshot) {
        isAliveForSnapshot = aliveForSnapshot;
    }
}
