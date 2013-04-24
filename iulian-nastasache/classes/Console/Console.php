<?php
/**
 * Created by JetBrains PhpStorm.
 * User: iulian
 * Date: 4/19/13
 * Time: 5:38 PM
 * Console
 * ****** Pattern: Singleton
 */
class Console
{

    private static $instance;
    private static $tty;

    public static function CreateConsole()
    {
        if (subStr(PHP_OS, 0, 3) == "WIN") {
            self::$tty = fOpen("\con", "rb");
        } else {
            self::$tty = fOpen("php://stdin", "r");
        }

        if (!isset(self::$instance)) {
            //echo "Create console\n";
            $className = __CLASS__;
            self::$instance = new $className;
        }
        return self::$instance;
    }

    public function ReadConsole($length = 1024)
    {

        return tRim(fGets(self::$tty, $length));

    }

}