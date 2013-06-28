package first.example;

/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/29/13
 * Time: 6:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args){
        Employee employee = new Employee();
        employee.setType(new Engineer());
        System.out.println(employee.payAmount());
    }
}
