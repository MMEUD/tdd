<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Cristina Condruz
 * Date: 4/19/13
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
require_once('CommandExpression.php');
require_once('NsExpression.php');
require_once('SetExpression.php');
require_once('GetExpression.php');
require_once('ListExpression.php');

class Evaluator implements CommandExpression{
 private $syntaxTree;

 public function __construct($expression){
     $tokens = explode(" ", trim($expression));
     $nrArgs = sizeOf($tokens);
     if($nrArgs == 0){
       echo "Please insert a command!";
       return;
     }else{
       $command = $tokens[0];
       $paramsArr = array_shift($tokens);
       $params = implode(" ", $tokens);
       if($command == "ns"){
         $syntaxTree = new NsExpression($params);
       }
       else if($command == "set"){
         $syntaxTree = new SetExpression($params);
       }
       else if($command == "get"){
         $syntaxTree = new GetExpression($params);
       }
       else if($command == "list"){
         $syntaxTree = new ListExpression($params);
       }
       else if($command == "load"){
         $syntaxTree = new LoadExpression($params);
       }
       else if($command == "save"){
         $syntaxTree = new SaveExpression($params);
       }
       else{
         echo "Please insert a valid command!";
         return;
       }
     }
     $this->syntaxTree = $syntaxTree;
 }

 public function interpret(){
     return $this->syntaxTree->interpret();
 }

 public function __toString(){
     return "";
 }
}
