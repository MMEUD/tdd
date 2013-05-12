package universe;

/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/11/13
 * Time: 9:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class XAxis implements Axis {

    int axis;

    public XAxis(int axis) {
        this.axis = axis;
    }

    @Override
    public void setAxis(int location) {
        this.axis = location;
    }

    @Override
    public int getAxis() {
        return this.axis;
    }
}
