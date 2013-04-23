<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Cristina Condruz
 * Date: 4/23/13
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */

//require_once('../src/Commander.php');
class CommanderTest extends PHPUnit_Framework_TestCase{
  public function test1() {
      $p = "value";//new Property("name", "value");
     // $this->assertEquals($p->getName(), "name");
      $this->assertEquals($p, "value");
  }

}
