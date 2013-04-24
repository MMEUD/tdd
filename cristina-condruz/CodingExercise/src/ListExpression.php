<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Cristina Condruz
 * Date: 4/19/13
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */
require_once('CommandExpression.php');
require_once('NamespaceProperty.php');
require_once('MySingleton.php');

class ListExpression implements CommandExpression{
   private $argument;
   public function __construct($argument){
       $this->argument = $argument;
   }
   public function setArgument($argument){
         $this->argument = $argument;
   }
   public function getArgument(){
           return $this->argument;
   }
  public function interpret(){
      //the command should be get {parameter_name}
      //to verify if args correspond
      $currentSession = MySingleton::getInstance();
      $currentNamespace = $currentSession->getCurrentNamespace();
      $nmsProperties = $currentNamespace->getProperties();
      print_r($nmsProperties);
      if(array_key_exists($this->getArgument(),$nmsProperties)){
        return $currentNamespace->getName()." : ". $nmsProperties[$this->getArgument()]->getName() ." = ". $nmsProperties[$this->getArgument()]->getValue();
      }else{
        return "There is no parameter named: ".$this->getArgument()." in current namespace: ".$currentNamespace->getName();
      }
    }
}
