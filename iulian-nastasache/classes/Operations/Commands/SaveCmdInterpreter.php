<?php
/**
 * Created by JetBrains PhpStorm.
 * User: iulian
 * Date: 4/19/13
 * Time: 5:36 PM
 * Save IniGroups to the files command
 */
class SaveCmdInterpreter extends CmdInterpreter implements ICmdInterpreter
{

    public function CommandInterpreter()
    {

        if(coUnt(self::$params)== 0) {

            // Save all IniGroups
            foreach(IniGroupsContainer::ListIniGroupsNames() as $iniGroupName){

                IniGroupsContainer::SavePropertiesToFile($iniGroupName);
                $message =  "$iniGroupName : saved " .IniGroupsContainer::GetIniGroupByName($iniGroupName)->GetPropertiesCount()." parameters \n";
            }

        } else {

            // Save given IniGroup
            $iniGroupName = self::$params[0];
            IniGroupsContainer::SavePropertiesToFile($iniGroupName);

            $message = "$iniGroupName : saved " .IniGroupsContainer::GetIniGroupByName($iniGroupName)->GetPropertiesCount()." parameters \n";

        }

        return $message;
    }

}