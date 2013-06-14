<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Cristina Condruz
 * Date: 6/12/13
 * Time: 5:25 PM
 * To change this template use File | Settings | File Templates.
 */

require_once('Movie.php');

abstract class Price{
  abstract function getPriceCode();
  /**
   * @param $daysRented
   * @return float|int
   */

  abstract function getCharge($daysRented);

  /**
    * @param $daysRented
    * @return int
    */
   public function getFrequentRenterPoints($daysRented) {
      return 1;
   }
}

class RegularPrice extends Price{
 function getPriceCode(){
   return Movie::$REGULAR;
 }
  public function getCharge($daysRented){
    $result = 2;
    if ($daysRented > 2)
      $result += ($daysRented - 2) * 1.5;
    return $result;
  }
}

class NewReleasePrice extends Price{
 function getPriceCode(){
   return Movie::$NEW_RELEASE;
 }
  public function getCharge($daysRented){
    $result = $daysRented * 3;
    return $result;
  }
  public function getFrequentRenterPoints($daysRented) {
    return ($daysRented > 1) ? 2 : 1;
  }
}

class ChildrenPrice extends Price{
 function getPriceCode(){
   return Movie::$CHILDREN;
 }
  public function getCharge($daysRented){
    $result = 1.5;
    if ($daysRented > 3)
      $result += ($daysRented - 3) * 1.5;
    return $result;
  }
}
