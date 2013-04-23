<?php

class Console {
	var $tty;

	function Console() {
		if (substr(PHP_OS, 0, 3) == "WIN") {
			$this->tty = fOpen("\con", "rb");
		} else {
			if (!($this->tty = fOpen("/dev/tty", "r"))) {
			$this->tty = fOpen("php://stdin", "r");
			}
		}
	}

	function get($string, $length = 1024) {
		echo $string;
		$result = trim(fGets($this->tty, $length));
		echo "\n";
		return $result;
	}
}

?>