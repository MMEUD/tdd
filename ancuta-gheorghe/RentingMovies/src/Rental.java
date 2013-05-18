/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/18/13
 * Time: 5:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Rental {

    private Movie _movie;
    private int _daysRented;

    public Rental(Movie movie, int daysRented) {
        this._movie = movie;
        this._daysRented = daysRented;
    }

    public Movie getMovie() {
        return _movie;
    }

    public int getDaysRented() {
        return _daysRented;
    }

    public int getFrequentRenterPoints() {
        if ((getMovie().getPriceCode() == Movie.NEW_RELEASE)
                && getDaysRented() > 1)
            return 2;
        else return 1;
    }
}
