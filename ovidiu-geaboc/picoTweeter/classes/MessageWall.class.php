<?php
/**
 * Created by JetBrains PhpStorm.
 * User: ovidiu
 * Date: 6/27/13
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */

class MessageWall {

    private $messageStore;
    private $sortStrategy = SORT_BY_DATE_DESC;

    public function MessageWall($MessageStore)
    {
        $this->messageStore = $MessageStore;
    }

    public function show()
    {
        $messageCollection = $this->messageStore->readAll();
        foreach($messageCollection as $messageObject)
        {
            echo $messageObject->getFormattedText();
        }
    }
}
