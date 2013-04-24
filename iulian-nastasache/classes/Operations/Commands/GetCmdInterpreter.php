<?php
/**
 * Created by JetBrains PhpStorm.
 * User: iulian
 * Date: 4/19/13
 * Time: 5:36 PM
 *  Get given property of a IniGroup
 */
class GetCmdInterpreter extends CmdInterpreter implements ICmdInterpreter
{

    public function CommandInterpreter()
    {

       $iterator = IniGroupsContainer::GetCurentIniGroup()->ReadProperties();
       $property = self::ReadProperty($iterator,self::$params[0]);

       echo IniGroupsContainer::GetCurentIniGroupName()." : ".self::$params[0]." = ".$property[self::$params[0]]."\n";
    }

}