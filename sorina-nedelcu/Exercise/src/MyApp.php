<?
require_once "/Interpreter.php";

class MyApp {
  public static function main()  {
    $getOut = FALSE;
    $stdIn = fopen('php://stdin', 'r');
    echo "Add commands! Press Q to quit...\n\n";

    $expr = new Interpreter("Start");
    echo $expr->msg;

    while(!$getOut) {
      $line = trim(fread($stdIn, 1024));

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