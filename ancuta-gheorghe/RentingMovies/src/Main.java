/**
 * Created with IntelliJ IDEA.
 * User: ancuta
 * Date: 5/18/13
 * Time: 6:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args){
        Movie movie1 = new Movie("Star Wars", 1);
        Movie movie2 = new Movie("Battlestar Galactica", 2);

        Rental rental1 = new Rental(movie1, 2);
        Rental rental2 = new Rental(movie2, 7);

        Customer customer = new Customer("Ancuta");
        customer.addRental(rental1);
        customer.addRental(rental2);

        System.out.println(customer.statement());
        System.out.println();

    }
}
