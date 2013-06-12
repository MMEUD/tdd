<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Cristina Condruz
 * Date: 5/28/13
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 */
class Movie{
  public static $CHILDREN = 2;
  public static $REGULAR = 0;
  public static $NEW_RELEASE = 1;

  private $_title;
  private $_priceCode;

  function __construct($_priceCode, $_title) {
    $this->_priceCode = $_priceCode;
    $this->_title = $_title;
  }

  public function setPriceCode($priceCode) {
    $this->_priceCode = $priceCode;
  }

  public function getPriceCode() {
    return $this->_priceCode;
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
    $result = 0;
    switch ($this->getPriceCode()) {
      case self::$REGULAR:
        $result += 2;
        if ($daysRented > 2)
          $result += ($daysRented - 2) * 1.5;
        break;
      case  self::$NEW_RELEASE:
        $result += $daysRented * 3;
        break;
      case self::$CHILDREN:
        $result += 1.5;
        if ($daysRented > 3)
          $result += ($daysRented - 3) * 1.5;
        break;
    }
    return $result;
   }

  /**
   * @param $daysRented
   * @return int
   */
  public function getFrequentRenterPoints($daysRented) {
     if (($this->getPriceCode() == self::$NEW_RELEASE) && $daysRented > 1)
       return 2;
     else
       return 1;
  }
}