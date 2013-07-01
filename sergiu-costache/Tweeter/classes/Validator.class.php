<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Administrator
 * Date: 27.06.2013
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */

class Validator {

    function Validator() {}

    function isValid($content) {
        return (strlen($content) <= 5);
    }
}