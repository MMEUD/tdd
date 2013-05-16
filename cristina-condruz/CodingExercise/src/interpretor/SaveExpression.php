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

class SaveExpression implements CommandExpression{
   private $argument;
   private $msg;
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
      //the command should be save or save {namespace_name}
      $currentSession = MySingleton::getInstance();
      $allNamespaces = $currentSession->getNamespaces();
      if ($this->getArgument() != ""){
        //save all the properties of {namespace_name} into {namespace_name}.properties
        if(array_key_exists($this->getArgument(),$allNamespaces)){
          $selectedNamespace = $currentSession->getNamespace($this->getArgument());
          $allPropertiesOfNamespace = $selectedNamespace->getProperties();
          //to implement
        }else{
          $this->msg =  "There is no namespace named: ".$this->getArgument();
        }
      }else{
        //save -- stores all the properties of a each namespace into {namespace_name}.properties
        ksort($allNamespaces);//// SORT_NATURAL | SORT_FLAG_CASE
        foreach($allNamespaces as $selectedNamespace){
          $allPropertiesOfNamespace = $selectedNamespace->getProperties();
          //to implement
        }
      }
      return $this->msg;
    }
}
