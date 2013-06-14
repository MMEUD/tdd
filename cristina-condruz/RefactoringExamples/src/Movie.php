<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Cristina Condruz
 * Date: 5/28/13
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 */
require_once('Price.php');

class Movie{
  public static $CHILDREN = 2;
  public static $REGULAR = 0;
  public static $NEW_RELEASE = 1;

  private $_title;
  private $_priceCode;
  private $_price; //Price type

  function __construct($_priceCode, $_title) {
    $this->setPriceCode($_priceCode);
    $this->_title = $_title;
  }

  public function setPriceCode($priceCode) {
    switch ($priceCode) {
       case Movie::$REGULAR:
         $this->_price = new RegularPrice();
         break;
       case  Movie::$NEW_RELEASE:
         $this->_price = new NewReleasePrice();
         break;
       case Movie::$CHILDREN:
         $this->_price = new ChildrenPrice();
         break;
      default:
        throw new Exception("Incorrect Price Code.");
     }
  }

  public function getPriceCode() {
    return $this->_price->getPriceCode();
  }

  public function setTitle($title) {
    $this->_title = $title;
  }

  public function getTitle() {
    return $this->_title;
  }

  /**
   * @param $daysRented
   * @return float|int
   */

  public function getCharge($daysRented){
    return $this->_price->getCharge($daysRented);
   }

  /**
   * @param $daysRented
   * @return int
   */
  public function getFrequentRenterPoints($daysRented) {
     return $this->_price->getFrequentRenterPoints($daysRented);
  }
}