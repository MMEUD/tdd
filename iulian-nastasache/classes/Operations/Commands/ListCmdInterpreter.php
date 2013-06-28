<?php
/**
 * Created by JetBrains PhpStorm.
 * User: iulian
 * Date: 4/19/13
 * Time: 5:35 PM
 * List all or given IniGroup(s) properties
 */
class ListCmdInterpreter extends CmdInterpreter implements ICmdInterpreter
{

    public function CommandInterpreter()
    {
        $message = "";

        if(coUnt(self::$params)== 0) {

            $groupsNames = array_Keys(IniGroupsContainer::ListIniGroups());

            aSort($groupsNames); /// Sort IniGroups

            foreach($groupsNames as $groupName) {

                $iniGroup = IniGroupsContainer::GetIniGroupByName($groupName);

                $message .= $this->OutPutIniGroupProperties($groupName,$iniGroup);

            }

        } else {

            $groupName = self::$params[0];
            $iniGroup = IniGroupsContainer::GetIniGroupByName($groupName);

            $message .= $this->OutPutIniGroupProperties($groupName,$iniGroup);

        }

        return $message;

    }

    private function OutPutIniGroupProperties($groupName,$group){


        $iterator = $group->ReadProperties();
        $properties = array();

        $message = "";

        /** @noinspection PhpUndefinedMethodInspection */
        /** @noinspection PhpUndefinedMethodInspection */
        /** @noinspection PhpUndefinedMethodInspection */
        /** @noinspection PhpUndefinedMethodInspection */
        while($iterator->valid()) {

            $properties[$iterator->current()->GetKey()] = $iterator->current()->GetValue();

            $iterator->next();
        }

        kSort($properties); /// Sort properties

        foreach($properties as $key => $value) {
            $message .= $groupName." : ".  $key." = ".$value."\n";
        }

        return $message;
    }

}