<?php
/**
 * Created by JetBrains PhpStorm.
 * User: iulian
 * Date: 4/19/13
 * Time: 5:37 PM
 * Simple properties definition class
 */
class Property
{

    private $key;
    private $value;

    public function __construct($key, $value)
    {
        $this->setKey($key);
        $this->setValue($value);
    }

    private function SetKey($key)
    {
        $this->key = $key;
    }

    public function SetValue($value)
    {
        $this->value = $value;
    }

    public function GetKey()
    {
        return $this->key;
    }

    public function GetValue()
    {
        return $this->value;
    }

}