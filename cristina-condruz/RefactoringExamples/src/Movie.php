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
}