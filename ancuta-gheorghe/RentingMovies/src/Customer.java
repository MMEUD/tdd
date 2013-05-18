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
        Enumeration rentals = _rentals.elements();
        String result = "Rental Record for " + getName() + "\n";
        while (rentals.hasMoreElements()){
            Rental each = (Rental) rentals.nextElement();
            result += "\t" + each.getMovie().getTitle() + "\t" +
                    String.valueOf(each.getCharge()) + "\n";
        }
        result += "Amount owed is " + String.valueOf(getTotalCharge()) + "\n";
        result += "You earned " + String.valueOf(getTotalFrequentRenterPoints()) +
                " frequent renter points";
        return result;
    }

    public String htmlStatement(){
        Enumeration rentals = _rentals.elements();
        String result = "<H1>Rental Record for <EM>" + getName() + "</EM></H1>\n";
        while (rentals.hasMoreElements()){
            Rental each = (Rental) rentals.nextElement();
            result += "\t" + each.getMovie().getTitle() + "\t" +
                    String.valueOf(each.getCharge()) + "<BR>\n";
        }
        result += "Amount owed is " + String.valueOf(getTotalCharge()) + "\n";
        result += "You earned <EM>" + String.valueOf(getTotalFrequentRenterPoints()) +
                "</EM> frequent renter points";
        return result;
    }

    public double getTotalCharge() {
        double result = 0;
        Enumeration rentals = _rentals.elements();
        while (rentals.hasMoreElements()){
            Rental each = (Rental) rentals.nextElement();
            result += each.getCharge();
        }
        return result;
    }

    public double getTotalFrequentRenterPoints() {
        double result = 0;
        Enumeration rentals = _rentals.elements();
        while (rentals.hasMoreElements()){
            Rental each = (Rental) rentals.nextElement();
            result += each.getFrequentRenterPoints();
        }
        return result;
    }

}
