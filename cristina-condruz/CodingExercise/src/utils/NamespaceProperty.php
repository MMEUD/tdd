<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Cristina Condruz
 * Date: 4/22/13
 * Time: 1:16 PM
 * To change this template use File | Settings | File Templates.
 */
class NamespaceProperty {
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
