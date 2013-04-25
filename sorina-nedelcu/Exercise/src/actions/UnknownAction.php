<?php
require_once 'Action.php';
require_once '/ContextSingleton.php';
require_once '/util/NamespaceObject.php';

class UnknownAction implements Action {
  private $context; //  ContextSingleton

  public function __construct() {
    $this->context = ContextSingleton::getInstance();
  }

  public function doAction($param) {
    if($param[0]=='Q' || $param[0]=='q')
      $msg = "Q";
    else
      $msg = "Unknown command.\n";

    return $msg;
  }
}
