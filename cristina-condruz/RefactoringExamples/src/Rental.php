<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Cristina Condruz
 * Date: 5/28/13
 * Time: 11:34 AM
 * To change this template use File | Settings | File Templates.
 */
require_once('Movie.php');
class Rental {
  private $_movie;//Movie type
  private $_daysRented;

  function __construct($_daysRented, Movie $_movie) {
    $this->_daysRented = $_daysRented;
    $this->_movie = $_movie;
  }

  public function getDaysRented() {
    return $this->_daysRented;
  }

  public function getMovie() {
    return $this->_movie;
  }

  /**
   * @return int
   */
  function getFrequentRenterPoints() {
     return $this->_movie->getFrequentRenterPoints($this->_daysRented);
  }

  /**
   * @return float|int
   */
  public function getCharge(){
       return $this->_movie->getCharge($this->_daysRented);
  }

}
