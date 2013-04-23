<?php

class OperationFactory {
    public static function build($type, $args) {
        // assumes the use of an autoloader
        $operation = "Operation_" . $type;
        if (!class_exists($operation)) {
            throw new Exception("Invalid operation given");
        }
		return new $operation($args);
    }
}

/*	Analize the give command and choose appropriate Factory Method if found
 */
class Evaluator {
	public $_msg = '';
	public $_interpretedOperation;
	
    public function Evaluator($command) {
		
		// filter possible spaces between arguments so that we can deal with 'cmd arg1 arg2 ...'
		$operationPieces = array_values(array_filter(explode(' ', trim($command))));
		$operation = false;
		
		if (isset($operationPieces[0])) $verb = $operationPieces[0];
		else return false;
		
		try {
			$operation = OperationFactory::build($verb, $operationPieces);
			$operation->interpret();
		} catch (Exception $e) {
			$this->_msg = $e->getMessage();
		}
		$this->_interpretedOperation = $operation;
		
		return true;
    }
}

// interpreter abstract class
abstract class Operation {
	protected $args;
	public $returnMessage = '';
	
	public function Operation($args) {
		$this->args = $args;
	}
	public abstract function interpret();
}

/* 
 *	Given the command 'ns tests' the evaluator finds Operation_[ns] class and instantiates it
 *	Constructor argument is the array(0=>'ns',1=>'tests')
 */
class Operation_ns extends Operation {
	
	// set the global namespace to the second argument if it is present
	public function interpret() {
		if (isset($this->args[1])) {
			Context::getInstance()->namespace_crt = $this->args[1];
			$this->returnMessage = "Current namespace: {$this->args[1]}";
		}
		return $this;
	}
}

/* 
 *	Given the command 'set param1 value1' the evaluator finds Operation_[set] class and instantiates it
 *	Constructor argument is the array(0=>'set',1=>'param1',2=>'value1')
 */
class Operation_set extends Operation {

	// set in the current namespace param1=value1
	public function interpret() {
		// ensure the correct grammar: set arg1 arg2
		if (count($this->args) != 3) {
			$this->returnMessage = "set : invalid number of arguments (set arg1 arg2)";
			return false;
		}
		$namespaces = Context::getInstance()->namespaces;
		$namespace_crt = Context::getInstance()->namespace_crt;
		
		if (!isset($namespaces[$namespace_crt])) {
			$namespaces[$namespace_crt] = array();
		}
		
		$namespaces[$namespace_crt][$this->args[1]] = $this->args[2];
		$this->returnMessage = "$namespace_crt : {$this->args[1]} = {$this->args[2]}";
		
		Context::getInstance()->namespaces = $namespaces;
		//var_dump(Context::getInstance()->namespaces);
		return true;
	}
}

/* 
 *	Given the command 'get param' the evaluator finds Operation_[get] class and instantiates it
 *	Constructor argument is the array(0=>'get',1=>'param')
 */
class Operation_get extends Operation {

	// get from the current namespace the value of given param
	public function interpret() {
		$namespaces = Context::getInstance()->namespaces;
		$namespace_crt = Context::getInstance()->namespace_crt;
		
		if (isset($namespaces[$namespace_crt][$this->args[1]])) {
			$this->returnMessage = "$namespace_crt : {$this->args[1]} = {$namespaces[$namespace_crt][$this->args[1]]}";
		} else {
			$this->returnMessage = "$namespace_crt : parameter '{$this->args[1]}' not set";
		}
		return true;
	}
}

/* 
 *	Given the command 'list [namespace]' the evaluator finds Operation_[list] class and instantiates it
 *	Constructor argument is the array(0=>'list'[optional ,1=>'namespace'])
 */
class Operation_list extends Operation {
	
	public function interpret() {
	
		if (isset($this->args[1])) {
			// if there is the namespace argument, list the given namespace only
			$this->listNamespace($this->args[1]);
		} else {
			// list all the namespaces ordered alphabetically by namespace, parameters
			$namespaces = Context::getInstance()->namespaces;
			ksort($namespaces);
			foreach ($namespaces as $namespace_crt => $namespace) {
				$this->listNamespace($namespace_crt);
			}
		}
		return true;
	}
	
	public function listNamespace($ns) {
		$namespaces = Context::getInstance()->namespaces;
		if (isset($namespaces[$ns])) {
			ksort($namespaces[$ns]);
			foreach ($namespaces[$ns] as $param => $value) {
				$this->returnMessage .= "$ns : $param = $value\n";
			}
		}
	}
}

/* 
 *	Given the command 'load [namespace]' the evaluator finds Operation_[load] class and instantiates it
 *	Constructor argument is the array(0=>'load'[optional ,1=>'namespace'])
 */
class Operation_load extends Operation {
	
	public function interpret() {
	
		if (isset($this->args[1])) {
			// if there is the namespace argument, load the given namespace only
			$this->loadNamespace($this->args[1]);
		} else {
			// load all the namespaces
			$this->loadAllNamespaces();
		}
		return true;
	}
	
	public function loadNamespace($ns) {
		$parameters = array();
		
		if (is_file($ns.'.ini')) {
			$lines = file($ns.'.ini', FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);
			//var_dump($lines);
			foreach ($lines as $line) {
				$param_value = explode('=', $line);
				if (count($param_value) == 2) {
					$parameters[$param_value[0]] = $param_value[1];
				}
			}
			Context::getInstance()->namespaces[$ns] = $parameters;
			$this->returnMessage .= "$ns : loaded ".count($parameters)." parameters\n";
		} else {
			$this->returnMessage = "file $ns.ini not found";
		}
	}
	
	public function loadAllNamespaces() {
		foreach (glob("*.ini") as $filename) {
			//echo "$filename size " . filesize($filename) . "\n";
			$this->loadNamespace(str_replace('.ini', '', $filename));
		}
	}
}

/* 
 *	Given the command 'save [namespace]' the evaluator finds Operation_[save] class and instantiates it
 *	Constructor argument is the array(0=>'save'[optional ,1=>'namespace'])
 */
class Operation_save extends Operation {

	public function interpret() {
	
		if (isset($this->args[1])) {
			// if there is the namespace argument, load the given namespace only
			$this->saveNamespace($this->args[1]);
		} else {
			// load all the namespaces
			$this->saveAllNamespaces();
		}
		return true;
	}
	
	public function saveNamespace($ns) {
		$parameters = $lines = array();
		
		$namespaces = Context::getInstance()->namespaces;
		if (isset($namespaces[$ns])) $parameters = $namespaces[$ns];
		
		foreach ($parameters as $param => $value) {
			$lines[] = $param .'='. $value;
		}
		
		$fp = fopen($ns.'.ini', "w");
		fputs($fp, join("\n", $lines));
		fclose($fp);
		$this->returnMessage .= "$ns : saved ".count($lines)." parameters\n";
		
	}
	
	public function saveAllNamespaces() {
		$namespaces = Context::getInstance()->namespaces;
		foreach ($namespaces as $ns => $array) {
			$this->saveNamespace($ns);
		}
	}
}

?>