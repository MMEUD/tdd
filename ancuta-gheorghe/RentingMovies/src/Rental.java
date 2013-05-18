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

    double getCharge() {
        double result = 0;
        switch (getMovie().getPriceCode()){
            case Movie.REGULAR :
                result += 2;
                if (getDaysRented() > 2)
                    result += (getDaysRented() - 2) * 1.5;
                break;
            case Movie.NEW_RELEASE:
                result += getDaysRented() * 2;
                break;
            case Movie.CHILDREN:
                result += 1.5;
                if (getDaysRented() > 3)
                    result += (getDaysRented() - 3) * 1.5;
                break;
        }
        return result;
    }
}
