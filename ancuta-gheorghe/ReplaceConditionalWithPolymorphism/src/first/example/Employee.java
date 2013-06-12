package first.example;

/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/29/13
 * Time: 6:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class Employee {

    public int payAmount(){
        return _type.payAmount();

    }

    int getType(){
        return _type.getTypeCode();
    }

    public void setType(EmployeeType _type) {
        this._type = _type;
    }

    private EmployeeType _type;
}
