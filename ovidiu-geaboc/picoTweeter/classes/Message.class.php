<?php
/**
 * Created by JetBrains PhpStorm.
 * User: ovidiu
 * Date: 6/27/13
 * Time: 1:31 PM
 * To change this template use File | Settings | File Templates.
 */

class Message
{
    private $text;
    private $author;
    private $timestamp;

    public function getText()
    {
        return $this->text;
    }

    public function getFormattedText() {
        return "<i>".$this->getText()."</i> from ".$this->author."@ ".$this->timestamp."<br />";
    }
    public function setText($text)
    {
        $this->text = $text;
        return $this->text;
    }

        public function Message($text='', $author ='anonymous coward')
    {
        $this->text = $text;
        $this->author = $author;
        $this->timestamp = time();
    }
}

class Validator
{
    public function isValid($message) {
       $messageLength = strlen(trim($message));

        return( $messageLength <= 140 && $messageLength > 0);
    }

}

