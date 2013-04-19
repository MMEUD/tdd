<?php

class MyAppTest extends PHPUnit_Framework_TestCase {

  public function test1()
      {
          $stack = array();
          $this->assertEquals(0, count($stack));

          array_push($stack, 'Test');
          $this->assertEquals('Test', $stack[count($stack)-1]);
          $this->assertEquals(1, count($stack));

          $this->assertEquals('Test', array_pop($stack));
          $this->assertEquals(0, count($stack));
      }
}
