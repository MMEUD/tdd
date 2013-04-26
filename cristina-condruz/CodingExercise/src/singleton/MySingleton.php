<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Cristina Condruz
 * Date: 4/22/13
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
require_once('/utils/CommandNamespace.php');

class MySingleton {

  private static $instance = null;
  private $currentNamespace = ""; //new CommandNamespace();
  private $namespaces = array();// array of CommandNamespace;

  private function __construct(){}

  public static function getInstance(){
     static $inst = null;
     if ($inst === null) {
         $inst = new MySingleton();
     }
     return $inst;
  }

  public function setCurrentNamespace($namespaceName) {
    $selectedNamespace = new CommandNamespace();
    $existingNamespaces = $this->getNamespaces();
    $namespaceExists = false;
    foreach($existingNamespaces as $currentNamespace){
      if($currentNamespace->getName() == $namespaceName){
        $namespaceExists =  true;
        $selectedNamespace = $currentNamespace;
      }
    }
    if($namespaceExists){
      $this->currentNamespace = $selectedNamespace;
    }else{
      $selectedNamespace->setName($namespaceName);
      $this->addNamespace($selectedNamespace);
      $this->setCurrentNamespace($namespaceName);
    }
  }

  public function getCurrentNamespace() {
   return $this->currentNamespace;
  }

  public function addNamespace($namespaceObj) {
    $this->namespaces[$namespaceObj->getName()] = $namespaceObj;
  }

  public function getNamespace($namespaceName){
    $selectedNamespace = new CommandNamespace();
    $existingNamespaces = $this->getNamespaces();
    foreach($existingNamespaces as $currentNamespace){
      if($currentNamespace->getName() == $namespaceName){
        $selectedNamespace = $currentNamespace;
      }
    }
	  return $selectedNamespace;
	}

  public function getNamespaces() {
      return $this->namespaces;
  }

	public function setNamespaces($namespaces) {
    $this->namespaces = $namespaces;
	}

  public function clearNamespaces(){
    $this->namespaces = "";
	}

}
?>