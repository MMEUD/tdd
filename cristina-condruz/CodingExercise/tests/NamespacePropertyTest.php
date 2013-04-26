<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Cristina Condruz
 * Date: 4/25/13
 * Time: 5:45 PM
 * To change this template use File | Settings | File Templates.
 */
require_once('../src/utils/NamespaceProperty.php');
class NamespacePropertyTest extends PHPUnit_Framework_TestCase{
  public function testNamespaceProperty() {
     $namespaceProperty = new NamespaceProperty("name","value");
     //$namespaceProperty->setName("Test");
     $this->assertEquals($namespaceProperty->getName(), "name");
     $this->assertEquals($namespaceProperty->getValue(), "value");
   }
}
