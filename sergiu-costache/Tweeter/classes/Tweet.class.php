<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Administrator
 * Date: 27.06.2013
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */

class tweet {
    private $message;
    private $timestamp;
    private $author;

    /**
     * @param mixed $author
     */
    public function setAuthor($author)
    {
        $this->author = $author;
    }

    /**
     * @return mixed
     */
    public function getAuthor()
    {
        return $this->author;
    }

    /**
     * @param mixed $message
     */
    public function setMessage($message)
    {
        $this->message = $message;
    }

    /**
     * @return mixed
     */
    public function getMessage()
    {
        return $this->message;
    }

    /**
     * @param mixed $timestamp
     */
    public function setTimestamp($timestamp)
    {
        $this->timestamp = $timestamp;
    }
    /**
     * @return mixed
     */
    public function getTimestamp()
    {
        return $this->timestamp;
    }


    function tweet($message)
    {
        $this->message = $message;
    }

}