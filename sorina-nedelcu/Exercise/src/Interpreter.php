<?php
require_once "/actions/FirstAction.php";
require_once "/actions/NsAction.php";
require_once "/actions/SetParameterAction.php";
require_once "/actions/GetParameterAction.php";
require_once "/actions/ListNamespaceAction.php";
require_once "/actions/LoadAction.php";
require_once "/actions/SaveAction.php";
require_once "/actions/UnknownAction.php";

class Interpreter {
	public $msg = '';
	public $interpretedOperation;

  public function Interpreter($line) {
    $action = new UnknownAction();

    $params = explode(" ", $line);
    if($params[0] == "Start") {
      $action = new FirstAction();          //Scenario 1 - first Action
    } else if($params[0] == "ns") {
      $action = new NsAction();             //Scenario 2 - change current Namespace
    } else if($params[0] == "set") {
      $action = new SetParameterAction();   //Scenario 3 - set parameters for current namespace
    } else if($params[0] == "get") {
      $action = new GetParameterAction();   //Scenario 4 - get parameters value for current namespace
    } else if($params[0] == "list") {
      $action = new ListNamespaceAction();  //Scenario 5,6 - list all namespace or list current namespace
    } else if($params[0] == "load") {
      $action = new LoadAction();           //Scenario 7, 9 - load from a file name
    } else if($params[0] == "save") {
      $action = new SaveAction();           //Scenario 8, 10 - save in a file name
    }

    $this->msg = $action->doAction($params);
		return $this->msg;
    }
}