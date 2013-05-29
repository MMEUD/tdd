package first.example;

/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/29/13
 * Time: 7:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class Engineer extends EmployeeType {
    @Override
    int getTypeCode() {
        return ENGINEER;
    }

    public int payAmount(){
        return 100;
    }
}
