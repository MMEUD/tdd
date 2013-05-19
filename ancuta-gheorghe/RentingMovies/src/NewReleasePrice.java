/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/19/13
 * Time: 8:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewReleasePrice extends Price {

    int getPriceCode() {
        return Movie.NEW_RELEASE;
    }

    double getCharge(int daysRented) {
        return daysRented * 2;
    }
}
