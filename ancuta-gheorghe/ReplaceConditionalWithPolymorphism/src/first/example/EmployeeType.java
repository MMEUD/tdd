package first.example;

/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/29/13
 * Time: 6:58 AM
 * To change this template use File | Settings | File Templates.
 */
abstract class EmployeeType {

    public static final int ENGINEER = 1;
    public static final int SALESMAN = 2;

    abstract int getTypeCode();
    abstract int payAmount();

}
