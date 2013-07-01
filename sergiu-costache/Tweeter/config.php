<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Administrator
 * Date: 27.06.2013
 * Time: 14:52
 * To change this template use File | Settings | File Templates.
 */

// 1 - SESSION; 2 - FILE; 3 - SESSION & FILE
$config = array();

$config['FILE'] = 'storage.txt';
$config['STORAGE_TYPE'] = array(
    'SessionStorage',
	'FileStorage',
	//...
);