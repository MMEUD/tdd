<?
require_once "/ContextSingleton.php";
require_once "/Interpreter.php";
require_once "/actions/FirstAction.php";
require_once "/actions/NsAction.php";
require_once "/actions/SetParameterAction.php";
require_once "/actions/GetParameterAction.php";
require_once "/actions/ListNamespaceAction.php";
require_once "/actions/LoadAction.php";


class MyApp
{
  public static function main() //
  {
    $getOut = FALSE;
    $stdIn = fopen('php://stdin', 'r');
    echo "Add commands! Press Q to quit...\n\n";

    //Scenario 1 - set current Namespace to General
//    $firstAction = new FirstAction();
//    $firstAction->doAction('');
//
    $expr = new Interpreter("Start");
    echo $expr->msg;

    while(!$getOut) {
      $line = fread($stdIn, 1024);
      $line = trim($line);

      $expr = new Interpreter($line);
      if ($expr->msg!="Q")
        echo $expr->msg;
      else {
        echo "Quit!\n";
        $getOut = TRUE;  //Quit
      }
    }
    return(0);
  }
}

MyApp::main();
?>