<?php
require_once 'ContextSingleton.php';
require_once 'actions/FirstAction.php';
require_once 'actions/NsAction.php';


class MyApp
{
  public static function main() //
  {
    $getOut = FALSE;
    $stdIn = fopen('php://stdin', 'r');
    echo "Add commands! Press Q to quit...\n\n";

    //init ContextSingleton - Singleton
    $contextSingleton = ContextSingleton::getInstance();
    //Scenario 1 - set current Namespace to General
    $firstAction = new FirstAction($contextSingleton);
    $firstAction->doAction('');


    while(!$getOut) {
      $line = fread($stdIn, 1024);
      $line = trim($line);

      $params = explode(" ", $line);
      if($params[0] == "ns") {
        //Scenario 2 - change current Namespace
        $nsAction = new NsAction($contextSingleton);
        $nsAction->doAction($params);
      }

      //Quit
      if($line=='Q' || $line=='q') {
              echo "Quit!\n";
              $getOut = TRUE;
      }
    }
    return(0);
  }
}

MyApp::main();
?>