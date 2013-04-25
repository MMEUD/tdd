<?php
require_once 'Action.php';
require_once '/ContextSingleton.php';
require_once '/util/NamespaceObject.php';
require_once '/util/Property.php';

class SetParameterAction implements Action {
  private $context;  //ContextSingleton

  public function __construct() {
    $this->context = ContextSingleton::getInstance();
  }

  public function doAction($params) {
    $msg = "";
    if(count($params)<=2) {
      $msg = "Not enough parameters! Try again!\n";
    } else {
      //get current Namespace
      $currentNamespace = $this->context->getCurrentNamespace();
      $namespace = $this->context->getNamespaceObj($currentNamespace);
      $properties = $namespace->getProperties();

      $property = new Property($params[1], $params[2]);
      $properties[] = $property;

      $namespace->setProperties($properties);

      $this->context->addNamespace($currentNamespace,$namespace);
      $this->context->setCurrentNamespace($currentNamespace);

      for ($i=0; $i<count($properties); $i++) {
        $msg = $msg . "  " . $namespace->getName() . " : " . $properties[$i]->getName() . "=" . $properties[$i]->getValue() .  "\n";
      }
    }
  return $msg;
  }
}
