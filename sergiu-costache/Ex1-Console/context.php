<?php
class Singleton {
    protected static $instance = null;
	
	public $namespace_crt = 'general';
	public $namespaces = array();
	
    protected function __construct() {}
    protected function __clone() {}

    public static function getInstance() {
        if (!isset(static::$instance)) {
            static::$instance = new static;
        }
        return static::$instance;
    }
}
class Context extends Singleton {};

?>