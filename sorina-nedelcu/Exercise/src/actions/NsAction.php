<?php
require_once 'Action.php';
require_once '/util/NamespaceObject.php';
require_once '/ContextSingleton.php';

class NsAction implements Action {
  private $context;  //ContextSingleton

  public function __construct() {
    $this->context = ContextSingleton::getInstance();
  }

  public function doAction($params) {
    if(count($params)<=1) {
      $msg = "No name for namespace! Please enter a name for namespace.\n";
    } else {
      //create a new Namespace
      $newNamespace = new NamespaceObject();
      $newNamespace->setName($params[1]);

      $this->context->setCurrentNamespace($newNamespace->getName());
      // add Namespace only of not already exist
      if(!array_key_exists($params[1], $this->context->getNamespace()))
        $this->context->addNamespace($params[1],$newNamespace);

      $currentNamespace = $this->context->getCurrentNamespace();
      $msg = "Current namespace is now : " . $currentNamespace . "\n";
    }
  return $msg;
  }
}
