<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Sorina Nedelcu
 * Date: 4/19/13
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */
class NamespaceObject {

  private $name;
  private $properties = array();

  public function setName($name) {
    $this->name = $name;
  }

  public function getName() {
    return $this->name;
  }

  public function setProperties($properties) {
    $this->properties = $properties;
  }

  public function getProperties() {
    return $this->properties;
  }
}
