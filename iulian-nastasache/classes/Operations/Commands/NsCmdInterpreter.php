<?php
/**
 * Created by JetBrains PhpStorm.
 * User: iulian
 * Date: 4/19/13
 * Time: 5:36 PM
 * Change the current namespace by console value
 */
class NsCmdInterpreter extends CmdInterpreter implements ICmdInterpreter
{

    public function CommandInterpreter()
    {

        if(!empty(self::$params)) {

            $currIniGroup = self::$params[0];

            if( !in_Array(self::$params[0],IniGroupsContainer::ListIniGroupsNames()) ) {

                IniGroupsContainer::AddIniGroup($currIniGroup, new IniGroup());

            }

            IniGroupsContainer::SetCurentIniGroup($currIniGroup);

            return "Current namespace: ".IniGroupsContainer::GetCurentIniGroupName() ." \n";


        } else {

            return ("No IniGrup provided. Use last one \n");
        }

    }

}