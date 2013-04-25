<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Sorina Nedelcu
 * Date: 4/23/13
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
class Interpreter {
	public $msg = '';
	public $interpretedOperation;

  public function Interpreter($line) {

    $params = explode(" ", $line);
    if($params[0] == "Start") {
      $firstAction = new FirstAction();
      $this->msg = $firstAction->doAction('');
    } else if($params[0] == "ns") {
      //Scenario 2 - change current Namespace
      $nsAction = new NsAction();
      $this->msg = $nsAction->doAction($params);
    } else if($params[0] == "set") {
      //Scenario 3 - set parameters for current namespace
      $setParameterAction = new SetParameterAction();
      $this->msg = $setParameterAction->doAction($params);
    } else if($params[0] == "get") {
      //Scenario 4 - get parameters value for current namespace
      $getParameterAction = new GetParameterAction();
      $this->msg =$getParameterAction->doAction($params);
    } else if($params[0] == "list") {
      //Scenario 5 - list all namespace
      //Scenario 6 - list current namespace
      $listNamespaceAction = new ListNamespaceAction();
      $this->msg = $listNamespaceAction->doAction($params);
    }  else if($params[0] == "load") {
      //Scenario 4 - get parameters value for current namespace
      $loadAction = new LoadAction();
      $this->msg = $loadAction->doAction($params);
    } else if($line=='Q' || $line=='q') {
      $this->msg = "Q";
    } else {
      $this->msg = "Unknown command.\n";
    }

		return $this->msg;
    }
}