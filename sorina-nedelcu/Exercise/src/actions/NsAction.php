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

  private $namespace;  //NamespeceSingleton

  public function __construct($namespaceSingleton) {
      $this::setNamespace($namespaceSingleton);
  }

  public function doAction($params) {

    if($params[1]== null || $params[1] == '') {
      echo "No name for namespace! Please enter a name";
    } else {
      //create a new Namespace
      $newNamespace = new NamespaceObject();
      $newNamespace->setName($params[1]);

      $this->namespace->addNamespace($newNamespace);
      $this->namespace->setCurrentNamespace($newNamespace->getName());

      $currentNamespace = $this->namespace->getCurrentNamespace();
      echo "Current namespace is now : " . $currentNamespace . "\n";
    }
  }

  public function setNamespace($namespace) {
    $this->namespace = $namespace;
  }

  public function getNamespace() {
    return $this->namespace;
  }
}
