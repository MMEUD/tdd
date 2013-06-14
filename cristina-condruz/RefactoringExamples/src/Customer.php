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
    $rentals = $this->_rentals;
    $result = "Rental Record for " . $this->getName() . "\n";
    foreach ($rentals as $key => $each) {
      //show figures for this rental
      $result .= "\t" . $each->getMovie()->getTitle(). "\t" . ($each->getCharge()) . "\n";
    }
     //add footer lines
    $result .= "Amount owed is " . $this->getTotalCharge() .  "\n";
    $result .= "You earned " . $this->getTotalFrequentRenterPoints() . " frequent renter points";
    return $result;
  }

  /**
     * @return string
     */
    public function htmlStatement() {
      $rentals = $this->_rentals;
      $result = "<H1>Rentals for <EM>" . $this->getName() . "</EM></H1><P>\n";
      foreach ($rentals as $key => $each) {
        //show figures for this rental
        $result .= "\t" . $each->getMovie()->getTitle(). "\t" . ($each->getCharge()) . "<BR>\n";
      }
       //add footer lines
      $result .= "<P>You owe <EM>" . $this->getTotalCharge() .  "</EM><P>\n";
      $result .= "On this rental you earned <EM>" . $this->getTotalFrequentRenterPoints() . "</EM> frequent renter points<P>";
      return $result;
    }

  /**
   * @return int
   */
  private function getTotalCharge(){
    $totalAmount = 0;
    foreach ($this->_rentals as $key => $each) {
      $totalAmount += $each->getCharge();
    }
    return $totalAmount;
  }

  /**
   * @return int
   */
  private function getTotalFrequentRenterPoints(){
    $frequentRenterPoints = 0;
    foreach ($this->_rentals as $key => $each) {
      $frequentRenterPoints += $each->getFrequentRenterPoints();
    }
    return $frequentRenterPoints;
  }

}
