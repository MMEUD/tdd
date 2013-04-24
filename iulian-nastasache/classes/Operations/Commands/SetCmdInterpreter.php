<?php
/**
 * Created by JetBrains PhpStorm.
 * User: iulian
 * Date: 4/19/13
 * Time: 5:36 PM
 * Set properties of a IniGroup
 */
class SetCmdInterpreter extends CmdInterpreter implements ICmdInterpreter
{

    public function CommandInterpreter()
    {

        IniGroupsContainer::GetCurentIniGroup()->SetProperty(self::$params[0], self::$params[1]);

        $iterator = IniGroupsContainer::GetCurentIniGroup()->ReadProperties();

        $property = self::ReadProperty($iterator,self::$params[0]);

        echo IniGroupsContainer::GetCurentIniGroupName()." : ".self::$params[0]." = ".$property[self::$params[0]]."\n";

    }

}