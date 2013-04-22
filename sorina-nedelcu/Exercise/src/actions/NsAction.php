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
class NsAction implements Action {

  private $context;  //ContextSingleton

  public function __construct($contextSingleton) {
      $this::setContext($contextSingleton);
  }

  public function doAction($params) {

    if(count($params)<=1) {
      echo "No name for namespace! Please enter a name for namespace.\n";
    } else {
      //create a new Namespace
      $newNamespace = new NamespaceObject();
      $newNamespace->setName($params[1]);

      $this->context->addNamespace($newNamespace);
      $this->context->setCurrentNamespace($newNamespace->getName());

      $currentNamespace = $this->context->getCurrentNamespace();
      echo "Current namespace is now : " . $currentNamespace . "\n";
    }
  }

  public function setContext($context) {
    $this->context = $context;
  }

  public function getContext() {
    return $this->context;
  }


}
