<?php
/**
 * Created by JetBrains PhpStorm.
 * User: iulian
 * Date: 4/22/13
 * Time: 4:20 PM
 * A global container of all IniGroups and operations over IniGroups inside
 */
class IniGroupsContainer
{

    private static $currentIniGroup; // Current name of the object in use
    private static $IniGroups = Array(); // Namespaces names => objects


    /** Set current NameSpace by name */
    public static function SetCurentIniGroup($iniGroupName)
    {
        self::$currentIniGroup = $iniGroupName;

    }


    /** return all IniGroups names */
    public static function ListIniGroupsNames()
    {
        return array_Keys(self::$IniGroups);
    }


    /** return all IniGroups objects */
    public static function ListIniGroups()
    {
        return self::$IniGroups;
    }


    /** return name of IniGroup currently in use */
    public static function GetCurentIniGroupName()
    {
        return self::$currentIniGroup; // String

    }

    /** return IniGroup object currently in use */
    public static function GetCurentIniGroup()
    {
        return self::$IniGroups[self::$currentIniGroup];
    }

    /** return IniGroup by the name */
    public static function GetIniGroupByName($name)
    {
        foreach(self::$IniGroups as $iniGroupName => $iniGroup) {
            if($iniGroupName == $name) {
                return $iniGroup;
            }
        }
        return self::$currentIniGroup;

    }

    /** add a new IniGroup into container */
    public static function AddIniGroup($iniGroupName, $IniGroup)
    {
        self::$IniGroups[$iniGroupName] = $IniGroup;
    }



    /** Load properties of given IniGroup */
    public static function LoadPropertiesFromFile($iniGroupName)
    {
        $file = new FileReadWrite(CONFIG_FOLDER . $iniGroupName . INI_GROUP_EXTENSION);
        self::$IniGroups[$iniGroupName]->SetProperties($file->ReadFile());
    }


    /** Save properties of given IniGroup */
    public static function SavePropertiesToFile($iniGroupName)
    {

        $stringToWrite="";

        $iterator = self::$IniGroups[$iniGroupName]->ReadProperties();

        while($iterator->valid()) {
            $stringToWrite .= $iterator->current()->GetKey()." = ".$iterator->current()->GetValue()."\n";
            $iterator->next();
        }

        $file = new FileReadWrite(CONFIG_FOLDER . $iniGroupName . INI_GROUP_EXTENSION);
        $file->SetMode("w");
        $file->SetContent($stringToWrite);
        $file->WriteFile();

    }

}