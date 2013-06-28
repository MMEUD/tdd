/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/19/13
 * Time: 8:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegularPrice extends Price {

    int getPriceCode() {
        return Movie.REGULAR;
    }

    double getCharge(int daysRented) {
        double result = 2;
        if (daysRented > 2)
            result += (daysRented - 2) * 1.5;
        return result;
    }
}
