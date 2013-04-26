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
  private $properties = array();// array de NamespaceProperty

  public function setName($name) {
    $this->name = $name;
  }

  public function getName() {
    return $this->name;
  }

  public function setProperties($properties) {
    $this->properties = $properties;
  }

  public function addProperties($propertiesObj) {
     //if(array_key_exists($propertiesObj->getName(),$this->properties))
       $this->properties[$propertiesObj->getName()] = $propertiesObj;
  }

  public function getProperties() {
    return $this->properties;
  }
}
