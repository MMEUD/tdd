package first.example;

/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/29/13
 * Time: 7:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class Salesman extends EmployeeType {
    @Override
    int getTypeCode() {
        return SALESMAN;
    }

    public int payAmount(){
        return 99;
    }
}
