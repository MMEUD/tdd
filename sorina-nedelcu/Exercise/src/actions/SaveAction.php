<?php
require_once 'Action.php';
require_once '/ContextSingleton.php';
require_once '/util/NamespaceObject.php';
require_once '/util/Property.php';

class SaveAction implements Action {
  private $context;  //ContextSingleton

  public function __construct() {
    $this->context = ContextSingleton::getInstance();
  }

  public function doAction($params) {
    $msg =  "\n";
    if(count($params)<=1) {
      $allNamespaces = $this->context->getNamespace();
      foreach($allNamespaces as $param => $aNamespace) {
        $msg.= $this->saveFile($aNamespace);
      }
    } else {
      $ns = $this->context->getNamespaceObj($params[1]);
      $msg.= $this->saveFile($ns);
    }
    return $msg;
  }


  public function saveFile ($ns) {
    $msg = "";
    $lines = array();

    $properties = $ns->getProperties();
    for ($j=0; $j<count($properties); $j++) {
      $lines[] = $properties[$j]->getName() .'='. $properties[$j]->getValue();
    }

    $fp = fopen('files/'.$ns->getName().'.properties', "w");
    fputs($fp, join("\n", $lines));
    fclose($fp);

    $msg .= $ns->getName() . ": saved ".count($lines)." properties\n";

    return $msg;
 }
}
