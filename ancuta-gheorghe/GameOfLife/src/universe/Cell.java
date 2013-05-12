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
    private XAxis x;
    private YAxis y;
    private boolean isAlive = false;

    public Cell(XAxis x, YAxis y) {
        this.setX(x);
        this.setY(y);
        this.setAlive(true);
    }

    public Cell(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public XAxis getX() {
        return x;
    }

    public void setX(XAxis x) {
        this.x = x;
    }

    public YAxis getY() {
        return y;
    }

    public void setY(YAxis y) {
        this.y = y;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
