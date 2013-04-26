<?php
class Property {

  private $name;
  private $value;

  public function __construct($name, $value) {
    $this::setName($name);
    $this::setValue($value);
  }

  public function setName($name) {
    $this->name = $name;
  }

  public function getName() {
    return $this->name;
  }

  public function setValue($value) {
    $this->value = $value;
  }

  public function getValue() {
    return $this->value;
  }
}
