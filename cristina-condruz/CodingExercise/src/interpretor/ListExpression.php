<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Cristina Condruz
 * Date: 4/19/13
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */
require_once('CommandExpression.php');
require_once('/utils/NamespaceProperty.php');
require_once('/singleton/MySingleton.php');

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
      //the command should be list or list {namespace_name}
      $msg = "";
      $currentSession = MySingleton::getInstance();
      $allNamespaces = $currentSession->getNamespaces();
      if ($this->getArgument() != ""){
        //list {namespace_name}
        if(array_key_exists($this->getArgument(),$allNamespaces)){
          $selectedNamespace = $currentSession->getNamespace($this->getArgument());
          $allPropertiesOfNamespace = $selectedNamespace->getProperties();
          ksort($allPropertiesOfNamespace); // SORT_NATURAL | SORT_FLAG_CASE
          foreach($allPropertiesOfNamespace as $selectedProperty){
            $msg .= $selectedNamespace->getName()." : ".$selectedProperty->getName(). " = ". $selectedProperty->getValue()."\n";
          }
        }else{
          return "There is no namespace named: ".$this->getArgument();
        }
      }else{
        //list all namespaces ordered alphabetically
        ksort($allNamespaces);//// SORT_NATURAL | SORT_FLAG_CASE
        foreach($allNamespaces as $selectedNamespace){
          $allPropertiesOfNamespace = $selectedNamespace->getProperties();
          ksort($allPropertiesOfNamespace);// SORT_NATURAL | SORT_FLAG_CASE
          foreach($allPropertiesOfNamespace as $selectedProperty){
            $msg .= $selectedNamespace->getName()." : ".$selectedProperty->getName(). " = ". $selectedProperty->getValue()."\n";
          }
        }
      }
      return $msg;
    }
}
