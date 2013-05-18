/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/18/13
 * Time: 5:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class Movie {

    public static final int CHILDREN = 2;
    public static final int REGULAR = 0;
    public static final int NEW_RELEASE = 1;

    private String _title;
    private int _priceCode;

    public Movie(String title, int priceCode) {
        this._title = title;
        this._priceCode = priceCode;
    }

    public int getPriceCode() {
        return _priceCode;
    }

    public void setPriceCode(int arg) {
        this._priceCode = arg;
    }

    public String getTitle() {
        return _title;
    }

}
