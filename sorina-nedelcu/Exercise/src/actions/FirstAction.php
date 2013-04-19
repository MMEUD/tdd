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

  private $namespace; //  NamespaceSingleton

  public function __construct($namespaceSingleton) {
      $this::setNamespace($namespaceSingleton);
  }

  public function doAction($param) {
    //create a new Namespace
    $newNamespace = new NamespaceObject();
    $newNamespace->setName("General");
    //add the new namespace to the namespace list and set it as current namespace
    $this->namespace->addNamespace($newNamespace);
    $this->namespace->setCurrentNamespace($newNamespace->getName());
    //write current namespace
    $currentNamespace = $this->namespace->getCurrentNamespace();
    echo "Current namespace is : " . $currentNamespace . "\n\n";
  }

  public function setNamespace($namespace) {
    $this->namespace = $namespace;
  }

  public function getNamespace() {
    return $this->namespace;
  }
}
