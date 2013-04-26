<?php
require_once 'Action.php';
require_once '/ContextSingleton.php';
require_once '/util/NamespaceObject.php';
require_once '/util/Property.php';

class ListNamespaceAction implements Action {
  private $context;  //ContextSingleton

  public function __construct() {
    $this->context = ContextSingleton::getInstance();
  }

  public function doAction($params) {
    $msg =  "\n";
    if(count($params)<=1) {
      // get all namespaces
      $allNamespaces = $this->context->getNamespace();
      ksort($allNamespaces);

      foreach($allNamespaces as $param => $aNamespace) {
        $msg = $msg . " " . $aNamespace->getName() . "\n";

        $properties = $aNamespace->getProperties();
        for ($j=0; $j<count($properties); $j++) {
           $msg = $msg . "  " . $aNamespace->getName() . " : " . $properties[$j]->getName() . "=" . $properties[$j]->getValue() .  "\n";
        }
      }
    } else {
      // get namespace
      $currentNamespace = $this->context->getNamespaceObj($params[1]);
      $properties = $currentNamespace->getProperties();
      for ($j=0; $j<count($properties); $j++) {
         $msg = $msg . "  " . $currentNamespace->getName() . " : " . $properties[$j]->getName() . "=" . $properties[$j]->getValue() .  "\n";
      }
    }
    $msg = $msg . "\n";
    return $msg;
  }
}
