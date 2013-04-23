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
  private $ns;
  public function __construct($ns){
      $this->ns = $ns;
  }
  public function setNs($ns){
        $this->ns = $ns;
  }
  public function getNs(){
          return $this->ns;
  }
  public function interpret(){
      $currentSession = MySingleton::getInstance();
      $currentSession->setCurrentNamespace($this->getNs());
      return "Current namespace: ". $currentSession->getCurrentNamespace()->getName();
  }
  public function __toString(){
      return (string) $this->ns;
  }
}
?>
