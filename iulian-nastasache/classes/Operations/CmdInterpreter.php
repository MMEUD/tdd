<?php
/**
 * Created by JetBrains PhpStorm.
 * User: iulian
 * Date: 4/19/13
 * Time: 5:34 PM
 * Keep params of any command, common children functions
 */
abstract class CmdInterpreter
{
    protected static $params;

    // Add parameters to the command
    public function CmdInterpreter($params)
    {
        self::$params = $params;
    }

    // Prepare parameters for children required a
    protected function ReadProperty($iterator, $key){

        while($iterator->valid()) {
            if($iterator->current()->GetKey() == $key) {
                return array($iterator->current()->GetKey() => $iterator->current()->GetValue());
            }
            $iterator->next();
        }

    }


}