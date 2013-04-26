<?php
require_once 'Action.php';
require_once '/ContextSingleton.php';
require_once '/util/NamespaceObject.php';

class FirstAction implements Action {
  private $context; //  ContextSingleton

  public function __construct() {
    $this->context = ContextSingleton::getInstance();
  }

  public function doAction($param) {
    //create a new Namespace
    $newNamespace = new NamespaceObject();
    $newNamespace->setName("general");
    //add the new namespace to the namespace list and set it as current namespace
    $this->context->addNamespace("general", $newNamespace);
    $this->context->setCurrentNamespace($newNamespace->getName());
    //write current namespace
    $msg =  "Current namespace is : " . $this->context->getCurrentNamespace() . "\n\n";

    return $msg;
  }
}
