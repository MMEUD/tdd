<?php
set_Time_limit(0);
@ob_End_flush();
ob_Implicit_flush(true);

ini_Set('xdebug.show_exception_trace', 0);

deFine('MY_BASE_PATH', (string) ('/var/www/radu/tdd/iulian-nastasache'));
deFine('CONFIG_FOLDER','./configs/');
deFine('DEFAULT_NAMESPACE','general');
deFine('COMMANDS_FOLDER','./classes/Operations/Commands/');
deFine('INI_GROUP_EXTENSION','.properties');
deFine('QUIT_COMMAND','Q');

$path = (string) get_Include_path();
$path .= (string) (PATH_SEPARATOR . MY_BASE_PATH . '/classes/');
$path .= (string) (PATH_SEPARATOR . MY_BASE_PATH . '/classes/Console/');
$path .= (string) (PATH_SEPARATOR . MY_BASE_PATH . '/classes/Operations/');
$path .= (string) (PATH_SEPARATOR . MY_BASE_PATH . '/classes/Operations/Commands');
$path .= (string) (PATH_SEPARATOR . MY_BASE_PATH . '/classes/IniGroups/');
$path .= (string) (PATH_SEPARATOR . MY_BASE_PATH . '/classes/Utilities/');

set_Include_path($path);

spl_autoload_register(function ($className) {
    $className = (string) str_replace('\\', DIRECTORY_SEPARATOR, $className);
    include_once($className . '.php');
});
