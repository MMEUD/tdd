/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/18/13
 * Time: 5:45 PM
 * To change this template use File | Settings | File Templates.
 */

import java.util.*;

public class Customer {

    private String _name;
    private Vector _rentals = new Vector();

    public Customer(String name) {
        this._name = name;
    }

    public void addRental(Rental arg){
        _rentals.addElement(arg);
    }

    public String getName(){
        return _name;
    }

    public String statement(){
        double totalAmount = 0;
        int frequentRenterPoints = 0;
        Enumeration rentals = _rentals.elements();
        String result = "Rental Record for " + getName() + "\n";
        while (rentals.hasMoreElements()){
            double thisAmount = 0;
            Rental each = (Rental) rentals.nextElement();

            switch (each.getMovie().getPriceCode()){
                case Movie.REGULAR :
                    thisAmount += 2;
                    if (each.getDaysRented() > 2)
                        thisAmount += (each.getDaysRented() - 2) * 1.5;
                    break;
                case Movie.NEW_RELEASE:
                    thisAmount += each.getDaysRented() * 2;
                    break;
                case Movie.CHILDREN:
                    thisAmount += 1.5;
                    if (each.getDaysRented() > 3)
                        thisAmount += (each.getDaysRented() - 3) * 1.5;
                    break;
            }

            frequentRenterPoints++;
            if ((each.getMovie().getPriceCode() == Movie.NEW_RELEASE)
                && each.getDaysRented() > 1) frequentRenterPoints++;

            result += "\t" + each.getMovie().getTitle() + "\t" +
                    String.valueOf(thisAmount) + "\n";
            totalAmount += thisAmount;
        }
        result += "Amount owed is " + String.valueOf(totalAmount) + "\n";
        result += "You earned " + String.valueOf(frequentRenterPoints) +
                " frequent renter points";
        return result;
    }
}
