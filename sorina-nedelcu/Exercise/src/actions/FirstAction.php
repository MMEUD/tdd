<?php
require_once 'Action.php';
require_once '/util/NamespaceObject.php';

/**
 * Created by JetBrains PhpStorm.
 * User: Sorina Nedelcu
 * Date: 4/19/13
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
class FirstAction implements Action {

  private $context; //  ContextSingleton

  public function __construct($contextSingleton) {
      $this::setContext($contextSingleton);
  }

  public function doAction($param) {
    //create a new Namespace
    $newNamespace = new NamespaceObject();
    $newNamespace->setName("General");
    //add the new namespace to the namespace list and set it as current namespace
    $this->context->addNamespace($newNamespace);
    $this->context->setCurrentNamespace($newNamespace->getName());
    //write current namespace
    $currentNamespace = $this->context->getCurrentNamespace();
    echo "Current namespace is : " . $currentNamespace . "\n\n";
  }

  public function setContext($context) {
    $this->context = $context;
  }

  public function getContext() {
    return $this->context;
  }


}
