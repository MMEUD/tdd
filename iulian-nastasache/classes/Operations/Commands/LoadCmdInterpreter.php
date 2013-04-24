<?php
/**
 * Created by JetBrains PhpStorm.
 * User: iulian
 * Date: 4/19/13
 * Time: 5:35 PM
 * Load given or all IniGroup(s) properties from the files, inject to the IniGroup(s) ans show
 */
class LoadCmdInterpreter extends CmdInterpreter implements ICmdInterpreter
{

    public function CommandInterpreter()
    {

        if(!empty(self::$params) && self::$params[0] != "") {

            $iniGroupName = self::$params[0];


            $this->OutPutIniGroupLoads($iniGroupName);


        } else {

            foreach (new DirectoryIterator(CONFIG_FOLDER) as $fileInfo) {

                if ($fileInfo->isDot()) continue;

                $iniGroupName = $fileInfo->getBasename(INI_GROUP_EXTENSION);

                $this->OutputIniGroupLoads($iniGroupName);

            }
        }

    }


    private function OutputIniGroupLoads($iniGroupName){

        $iniGroupObj = new IniGroup ();
        IniGroupsContainer::AddIniGroup($iniGroupName,$iniGroupObj);
        IniGroupsContainer::LoadPropertiesFromFile($iniGroupName);

        echo $iniGroupName." loaded: ".$iniGroupObj->GetPropertiesCount()." parameters\n";
    }

}