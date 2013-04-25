<?php
require_once 'Action.php';
require_once '/ContextSingleton.php';
require_once '/util/NamespaceObject.php';
require_once '/util/Property.php';


/**
 * Created by JetBrains PhpStorm.
 * User: Sorina Nedelcu
 * Date: 4/22/13
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */
class LoadAction implements Action {

  private $context;  //ContextSingleton

  public function __construct() {
    $this->context = ContextSingleton::getInstance();
  }

  public function doAction($params) {
    $msg =  "\n";

    if(count($params)<=1) {
      foreach (glob("files/*.properties") as $filename) {
      			$filename = str_replace('.properties', '', $filename);
            $filename = str_replace('files/', '', $filename);

      			$this->loadFile($filename);
      		}



    } else {
      $this->loadFile($params[1]);
    }
    return $msg;
  }

  public function loadFile ($fileName) {
    if (is_file('files/'.$fileName.'.properties')) {
      $lines = file('files/'.$fileName.'.properties', FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);
      $this->loadNamespace($fileName, $lines);
    } else {
      $msg = "File $fileName.properties not found";
    }
 }

  public function loadNamespace($ns, $lines) {

    if(array_key_exists($ns, $this->context->getNamespace())) {
      $namespace = $this->context->getNamespaceObj($ns);
    } else {
      $namespace = new NamespaceObject();
      $namespace->setName($ns);
      $this->context->addNamespace($ns,$namespace);
    }

    $properties = $namespace->getProperties();

    foreach ($lines as $line) {
      $param_value = explode('=', $line);
      if (count($param_value) == 2) {
        $aProperty = new Property($param_value[0], $param_value[1]);
        if(isset($properties)) {
         if(!in_array($aProperty, $properties))
          $properties[] = $aProperty;
        }
        else
          $properties[] = $aProperty;
      }
    }

    $namespace->setProperties($properties);
    $this->context->addNamespace($ns,$namespace);
    $this->context->setCurrentNamespace($ns);
  }

}
