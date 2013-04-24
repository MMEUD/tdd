#!/usr/bin/php -q
<?php
require("BootStrap.php");

// Create default IniGroup and set it as current
IniGroupsContainer::AddIniGroup(DEFAULT_NAMESPACE, new IniGroup());
IniGroupsContainer::SetCurentIniGroup(DEFAULT_NAMESPACE);

// Create the console and wait for user inputs
$console = Console::CreateConsole();

echo "Enter something or '".QUIT_COMMAND."' to quit\n";

do {
    echo "Current namespace: ".IniGroupsContainer::GetCurentIniGroupName() ." \n";
    $commandString = $console->ReadConsole();

    // Open the interpreting to do something with command
    $interpreter = new Interpreter($commandString);
    $interpreter->Interpret();


} while (ucFirst($commandString) !== QUIT_COMMAND);
?>