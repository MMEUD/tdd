<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Claudia Diaconescu
 * Date: 6/27/13
 * Time: 12:08 PM
 * To change this template use File | Settings | File Templates.
 */

class User {
  private $id;
  private $firstName;
  private $lastName;

  function __construct($id,$firstName, $lastName) {
    $this->firstName = $firstName;
    $this->id = $id;
    $this->lastName = $lastName;
  }

  public function setFirstName($firstName) {
    $this->firstName = $firstName;
  }

  public function getFirstName() {
    return $this->firstName;
  }

  public function setId($id) {
    $this->id = $id;
  }

  public function getId() {
    return $this->id;
  }

  public function setLastName($lastName) {
    $this->lastName = $lastName;
  }

  public function getLastName() {
    return $this->lastName;
  }


}