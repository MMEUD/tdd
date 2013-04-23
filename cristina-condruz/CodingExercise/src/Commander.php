<?php
require_once('Evaluator.php');
require_once('MySingleton.php');
class Commander
{
        public static function main()
        {
                $getout = FALSE;
                $stdin = fopen('php://stdin', 'r');
                $currentSession = MySingleton::getInstance();
                $currentSession->setCurrentNamespace("general");
                echo "Current namespace: general\n";
                while(!$getout)
                {
                        $line = fread($stdin, 1024);
                        $line = trim($line);
                        if($line=='exit'){
                           echo "La revedere!\n";
                           $getout = TRUE;
                        }else{
                          $exprToEval = new Evaluator($line);
                          $strResult = $exprToEval->interpret();
                          echo "$strResult\n";
                        }

                }
                return(0);
        }
}


Commander::main();

?>