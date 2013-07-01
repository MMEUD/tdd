package universe;


/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/11/13
 * Time: 9:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class CellWorld extends Cell{

    private Cell n;
    private Cell ne;
    private Cell e;
    private Cell se;
    private Cell s;
    private Cell sv;
    private Cell v;
    private Cell nv;
	private int numberOfNeighbors;

    public CellWorld(int id) {
        super(id);
    }

    public Cell getN() {
        return n;
    }

    public void setN(Cell n) {
        this.n = n;
    }

    public Cell getNe() {
        return ne;
    }

    public void setNe(Cell ne) {
        this.ne = ne;
    }

    public Cell getE() {
        return e;
    }

    public void setE(Cell e) {
        this.e = e;
    }

    public Cell getSe() {
        return se;
    }

    public void setSe(Cell se) {
        this.se = se;
    }

    public Cell getS() {
        return s;
    }

    public void setS(Cell s) {
        this.s = s;
    }

    public Cell getSv() {
        return sv;
    }

    public void setSv(Cell sv) {
        this.sv = sv;
    }

    public Cell getV() {
        return v;
    }

    public void setV(Cell v) {
        this.v = v;
    }

    public Cell getNv() {
        return nv;
    }

    public void setNv(Cell nv) {
        this.nv = nv;
    }

    public int getNumberOfNeighbors(){
        calculateNumberOfNeighbors();
        return numberOfNeighbors;
    }

	private void calculateNumberOfNeighbors() {
		resetNumberOfNeighbors();
        if (neighborIsAlive(getN())) incrementNumberOfNeighbors();
        if (neighborIsAlive(getNe())) incrementNumberOfNeighbors();
        if (neighborIsAlive(getE())) incrementNumberOfNeighbors();
        if (neighborIsAlive(getSe())) incrementNumberOfNeighbors();
        if (neighborIsAlive(getS())) incrementNumberOfNeighbors();
        if (neighborIsAlive(getSv())) incrementNumberOfNeighbors();
        if (neighborIsAlive(getV())) incrementNumberOfNeighbors();
        if (neighborIsAlive(getNv())) incrementNumberOfNeighbors();
	}

	private void resetNumberOfNeighbors() {
		numberOfNeighbors = 0;
	}

	private int incrementNumberOfNeighbors() {
		return numberOfNeighbors++;
	}

	private boolean neighborIsAlive(Cell cell) {
		return cell != null && cell.isAlive();
	}
}
