package universe;

import java.util.ArrayList;

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
        int numberOfNeighbors = 0;
        if (this.getN() != null && this.getN().isAlive()) numberOfNeighbors++;
        if (this.getNe() != null && this.getNe().isAlive()) numberOfNeighbors++;
        if (this.getE() != null && this.getE().isAlive()) numberOfNeighbors++;
        if (this.getSe() != null && this.getSe().isAlive()) numberOfNeighbors++;
        if (this.getS() != null && this.getS().isAlive()) numberOfNeighbors++;
        if (this.getSv() != null && this.getSv().isAlive()) numberOfNeighbors++;
        if (this.getV() != null && this.getV().isAlive()) numberOfNeighbors++;
        if (this.getNv() != null && this.getNv().isAlive()) numberOfNeighbors++;
        return numberOfNeighbors;
    }
}
