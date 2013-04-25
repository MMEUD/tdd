<?php
require_once 'Action.php';
require_once '/ContextSingleton.php';
require_once '/util/NamespaceObject.php';
require_once '/util/Property.php';

class GetParameterAction implements Action {
  private $context;  //ContextSingleton

  public function __construct() {
    $this->context = ContextSingleton::getInstance();
  }

  public function doAction($params) {
    $msg = "";
    if(count($params)<=1) {
      $msg = "Not enough parameters! Try again!\n";
    } else {
      //get current Namespace
      $currentNamespace = $this->context->getCurrentNamespace();
      $namespace = $this->context->getNamespaceObj($currentNamespace);
      $properties = $namespace->getProperties();

      for ($i=0; $i<count($properties); $i++) {
        $proprName = $properties[$i]->getName();
        $proprValue = $properties[$i]->getValue();
        if($proprName == $params[1])
          $msg = $msg . "  " . $currentNamespace . " : " . $proprName . "=" . $proprValue .  "\n";
      }
    }
  return $msg;
  }
}