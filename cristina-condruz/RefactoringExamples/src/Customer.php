<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Cristina Condruz
 * Date: 5/28/13
 * Time: 11:38 AM
 * To change this template use File | Settings | File Templates.
 */
require_once('Rental.php');
class Customer {
  private $_name;
  private $_rentals = array();//array of Rental objects


  function __construct($_name) {
    $this->_name = $_name;
  }

  /**
   * @param Rental
   */
  function addRental(Rental $arg){
    $this->_rentals[] = $arg;
  }

  /**
   * @return String
   */
  public function getName() {
    return $this->_name;
  }

  /**
   * @return string
   */
public function statement() {
  $totalAmount = 0;
  $frequentRenterPoints = 0;
  $rentals = $this->_rentals;
  $result = "Rental Record for " . $this->getName() . "\n";
  //while (list($var, $val) = each($rentals)) {
  $each = ""; //new Movie();
  foreach ($rentals as $key => $val) {
    $thisAmount = 0;
    $each = $val;
    //determine amounts for each line
    switch ($each->getMovie()->getPriceCode()) {
       case Movie::$REGULAR:
         $thisAmount += 2;
         if ($each->getDaysRented() > 2)
            $thisAmount += ($each->getDaysRented() - 2) * 1.5;
         break;
       case  Movie::$NEW_RELEASE:
         $thisAmount += $each->getDaysRented() * 3;
         break;
       case Movie::$CHILDREN:
         $thisAmount += 1.5;
         if ($each->getDaysRented() > 3)
          $thisAmount += ($each->getDaysRented() - 3) * 1.5;
         break;
     }
    // add frequent renter points
    $frequentRenterPoints ++;
    if (($each->getMovie()->getPriceCode() == Movie::$NEW_RELEASE) &&  $each->getDaysRented() > 1)
      $frequentRenterPoints ++;
    //show figures for this rental
     $result .= "\t" . $each->getMovie()->getTitle(). "\t" . $thisAmount . "\n";
     $totalAmount += $thisAmount;
  }
   //add footer lines
  $result .= "Amount owed is " . $totalAmount .  "\n";
  $result .= "You earned " . $frequentRenterPoints . " frequent renter points";
  return $result;
}

}
