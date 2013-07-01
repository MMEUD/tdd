<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Administrator
 * Date: 27.06.2013
 * Time: 17:33
 * To change this template use File | Settings | File Templates.
 */

class Filter {
	private $filter;

	function Filter($filter)
	{
		$this->filter = $filter;
	}

	function getFilteredMessages($messages) {
		$filteredMessages = array();
		foreach ($messages as $message) {
			$message = unserialize($message);

			if ($message->getAuthor()->getHandler() == $this->filter) {
				$message = serialize($message);
				$filteredMessages[] = $message;
			}
		}

		return $filteredMessages;
	}
}

