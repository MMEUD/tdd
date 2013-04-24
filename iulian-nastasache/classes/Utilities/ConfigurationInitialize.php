<?php
/**
 * Created by JetBrains PhpStorm.
 * User: iulian
 * Date: 4/19/13
 * Time: 5:28 PM
 * Write a default iniGroup file
 */
class ConfigurationInitialize
{

    public function __construct($iniGroup)
    {
        $file = CONFIG_FOLDER . $iniGroup . INI_GROUP_EXTENSION;

        if (!is_File($file)) {

            $file = new FileReadWrite($file);
            $file->SetMode("w");
            $file->SetContent("");
            $file->WriteFile();
        }
    }


}