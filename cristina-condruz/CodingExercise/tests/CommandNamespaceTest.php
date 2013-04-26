<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Cristina Condruz
 * Date: 4/23/13
 * Time: 3:21 PM
 * To change this template use File | Settings | File Templates.
 */
require_once('../src/utils/CommandNamespace.php');
class CommandNamespaceTest extends PHPUnit_Framework_TestCase{
  public function testCommandNamespace() {
       $commandNamespace = new CommandNamespace();
       $commandNamespace->setName("Test");
       $this->assertEquals($commandNamespace->getName(), "Test");
   }
}
