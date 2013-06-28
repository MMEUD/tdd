<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Administrator
 * Date: 27.06.2013
 * Time: 14:52
 * To change this template use File | Settings | File Templates.
 */
session_start();
require 'config.php';
require 'classes/User.class.php';
require 'classes/Filter.class.php';
require 'classes/Storage.class.php';
require 'classes/Tweet.class.php';
require 'classes/Validator.class.php';

//$_SESSION = array();

$errorMessages = array();
$messages = array();

$storage = new MultipleStorage();
foreach ($config['STORAGE_TYPE'] as $storage_type) {
    $storage_crt = new $storage_type();
    $storage->addStorage($storage_crt);
}

$validator = new Validator();