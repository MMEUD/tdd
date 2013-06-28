<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Administrator
 * Date: 27.06.2013
 * Time: 17:05
 * To change this template use File | Settings | File Templates.
 */

class User {
	private $handler;
	private $name;

	function __construct($handler, $name)
	{
		$this->handler = $handler;
		$this->name = $name;
	}

	/**
	 * @return mixed
	 */
	public function getHandler()
	{
		return $this->handler;
	}

	/**
	 * @return mixed
	 */
	public function getName()
	{
		return $this->name;
	}

}