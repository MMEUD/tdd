<?php
set_time_limit(0);
@ob_end_flush();
ob_implicit_flush(true);

require 'console.php';
require 'context.php';
require 'interpreter.php';

//build the global singleton
$context = Context::getInstance();

echo "[q/Q] to quit\n";
echo "Current namespace: ".Context::getInstance()->namespace_crt."\n";
while (1) {
	$console = new Console();
	$cmdline = $console->get("[you@console] ");
	if (strtolower($cmdline) == "q") break;
	
	$evaluator = new Evaluator($cmdline);
	
	if(!empty($evaluator->_interpretedOperation->returnMessage)) echo $evaluator->_interpretedOperation->returnMessage . "\n";
	if(!empty($evaluator->_msg)) echo $evaluator->_msg . "\n";
}
echo "bye\n";

?>