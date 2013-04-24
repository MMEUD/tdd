<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Cristina Condruz
 * Date: 4/19/13
 * Time: 3:50 PM
 * To change this template use File | Settings | File Templates.
 */
require_once('CommandExpression.php');
require_once('MySingleton.php');

class NsExpression implements CommandExpression{
  private $argument;
  public function __construct($argument){
      $this->argument = $argument;
  }
  public function setArgument($argument){
        $this->argument = $argument;
  }
  public function getArgument(){
          return $this->argument;
  }
  public function interpret(){
    //to verify if args correspond
    $currentSession = MySingleton::getInstance();
    $currentSession->setCurrentNamespace($this->getArgument());
    return "Current namespace: ". $currentSession->getCurrentNamespace()->getName();
  }
}
?>
