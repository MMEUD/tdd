/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/19/13
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */

public class ChildrenPrice extends Price {

    int getPriceCode() {
        return Movie.CHILDREN;
    }

    double getCharge(int daysRented) {
        double result = 1.5;
        if (daysRented > 3)
            result += (daysRented - 3) * 1.5;
        return result;
    }
}
