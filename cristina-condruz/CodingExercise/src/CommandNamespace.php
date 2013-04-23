<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Cristina Condruz
 * Date: 4/22/13
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */
class CommandNamespace{
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
