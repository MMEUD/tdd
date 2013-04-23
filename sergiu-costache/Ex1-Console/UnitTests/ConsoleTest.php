<?php

require 'PHPUnit/autoload.php';
require 'context.php';
require 'interpreter.php';

class ConsoleTest extends PHPUnit_Framework_TestCase
{
	// using singleton ... makes setUp and tearDown useless
    public function setUp() {}
    public function tearDown() {}

    public function testShouldHaveGeneralNamespaceInIntitialState() {
		$this->assertEquals('general', Context::getInstance()->namespace_crt);
    }

	public function testInvalidCommand() {
		$evaluator = new Evaluator('missing_command');
		$this->assertEquals("Invalid operation given", $evaluator->_msg);
	}
	
    public function testChangeNamespace() {
		$evaluator = new Evaluator('ns test_namaspace'); 
		$this->assertEquals('test_namaspace', Context::getInstance()->namespace_crt);
    }

    public function testListReturnsEmptyCollection() {
		$evaluator = new Evaluator('list');
		$this->assertEquals('', $evaluator->_interpretedOperation->returnMessage);
    }

    public function testInvalidSetCommand() {
		$evaluator = new Evaluator('set');
		$this->assertEquals('set : invalid number of arguments (set arg1 arg2)', $evaluator->_interpretedOperation->returnMessage);
    }

    public function testInvalidSetCommand2() {
		$evaluator = new Evaluator('set param1');
		$this->assertEquals('set : invalid number of arguments (set arg1 arg2)', $evaluator->_interpretedOperation->returnMessage);
    }

    public function testInvalidSetCommand3() {
		$evaluator = new Evaluator('set param1 param2 param3');
		$this->assertEquals('set : invalid number of arguments (set arg1 arg2)', $evaluator->_interpretedOperation->returnMessage);
    }

    public function testSetCorrectParamValueInCurrentContext() {
		$evaluator = new Evaluator('set param1 value1');
		$this->assertEquals('test_namaspace : param1 = value1', $evaluator->_interpretedOperation->returnMessage);
    }

    public function testGetMissingParamValue() {
		$evaluator = new Evaluator('get dummy_param');
		$this->assertEquals('test_namaspace : parameter \'dummy_param\' not set', $evaluator->_interpretedOperation->returnMessage);
    }

    public function testGetParamValue() {
		$evaluator = new Evaluator('get param1');
		$this->assertEquals('test_namaspace : param1 = value1', $evaluator->_interpretedOperation->returnMessage);
    }
	
	public function testSaveFile() {
		$evaluator = new Evaluator('set param2 value2');
		$evaluator = new Evaluator('save test_namaspace');
		$this->assertEquals("test_namaspace : saved 2 parameters\n", $evaluator->_interpretedOperation->returnMessage);
	}
	
	public function testInvalidLoadFileCommand() {
		$evaluator = new Evaluator('load dummy');
		$this->assertEquals("file dummy.ini not found", $evaluator->_interpretedOperation->returnMessage);
	}
	
	public function testLoadFile() {
		$evaluator = new Evaluator('load test_namaspace');
		$this->assertEquals("test_namaspace : loaded 2 parameters\n", $evaluator->_interpretedOperation->returnMessage);
	}
	
    public function testChangeSecondNamespace() {
		$evaluator = new Evaluator('ns second_ns'); 
		$this->assertEquals('second_ns', Context::getInstance()->namespace_crt);
    }

    public function testSetCorrectParamValueInCurrentContext2() {
		$evaluator = new Evaluator('set p1 v1');
		$this->assertEquals('second_ns : p1 = v1', $evaluator->_interpretedOperation->returnMessage);
    }

    public function testListCommand() {
		$evaluator = new Evaluator('list');
		$this->assertEquals("second_ns : p1 = v1\ntest_namaspace : param1 = value1\ntest_namaspace : param2 = value2\n", $evaluator->_interpretedOperation->returnMessage);
    }

	public function testSaveFiles() {
		$evaluator = new Evaluator('save');
		$this->assertEquals("test_namaspace : saved 2 parameters\nsecond_ns : saved 1 parameters\n", $evaluator->_interpretedOperation->returnMessage);
	}
}

?>