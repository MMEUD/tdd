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

class Evaluator implements CommandExpression{
 private $syntaxTree;

 public function __construct($expression){
     $stack = array();
     $tokens = explode(" ", $expression);
     //process array !!!
     if($tokens[0] == "ns"){
       $syntaxTree = new NsExpression((isset($tokens[1])?$tokens[1]:(" ")));
     }
     else if($tokens[0] == "set"){
     }
     else{
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
