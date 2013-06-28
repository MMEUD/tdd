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
require_once('/strategy/StrategyContext.php');
class ListExpression implements CommandExpression{
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
      //the command should be list or list {namespace_name}
      $currentSession = MySingleton::getInstance();
      $allNamespaces = $currentSession->getNamespaces();
      if ($this->getArgument() != ""){
        //list {namespace_name}
        if(array_key_exists($this->getArgument(),$allNamespaces)){
          $selectedNamespace = $currentSession->getNamespace($this->getArgument());
          $allPropertiesOfNamespace = $selectedNamespace->getProperties();
          //ksort($allPropertiesOfNamespace); // SORT_NATURAL | SORT_FLAG_CASE
          //apply strategy pattern for sorting the properties
          $strategyContextForProp = new StrategyContext('A');
          $sortedProperties = $strategyContextForProp->getSortedArray($allPropertiesOfNamespace);
          foreach($sortedProperties as $selectedProperty){
            $this->msg .= $selectedNamespace->getName()." : ".$selectedProperty->getName(). " = ". $selectedProperty->getValue()."\n";
          }
        }else{
          $this->msg = "There is no namespace named: ".$this->getArgument();
        }
      }else{
        //list all namespaces ordered alphabetically
        //ksort($allNamespaces);//// SORT_NATURAL | SORT_FLAG_CASE
        //apply strategy pattern for sorting the namespaces
        $strategyContextForNms = new StrategyContext('A');
        $sortedNamespaces = $strategyContextForNms->getSortedArray($allNamespaces);
        foreach($sortedNamespaces as $selectedNamespace){
          $allPropertiesOfNamespace = $selectedNamespace->getProperties();
          //ksort($allPropertiesOfNamespace);// SORT_NATURAL | SORT_FLAG_CASE
           //apply strategy pattern for sorting the properties
          $strategyContextForProp = new StrategyContext('A');
          $sortedProperties = $strategyContextForProp->getSortedArray($allPropertiesOfNamespace);
          foreach($sortedProperties as $selectedProperty){
            $this->msg .= $selectedNamespace->getName()." : ".$selectedProperty->getName(). " = ". $selectedProperty->getValue()."\n";
          }
        }
      }
      return $this->msg;
    }
}
