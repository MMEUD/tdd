<?php

interface MessageStore
{
    function  write($message);
    function readAll();
    function init();
}

class MessageStoreMemory implements MessageStore
{
    public function MessageStoreMemory()
    {
        @session_start();
    }

    function write($message)
    {
        $_SESSION['messagestore'][] = serialize($message);
    }

    function readAll()
    {
        $messages = array();
        foreach ($_SESSION['messagestore'] as $serializedMessage)
        {
            $messages[]= unserialize($serializedMessage);
        }
        return $messages;
    }
    function init() {

        $_SESSION['messagestore']=array();

    }
}

class MessageStoreFile implements MessageStore
{
    public function MessageStoreFile()
    {
    }

    function write($message)
    {
    }

    function readAll()
    {
        $messages = array();
        return $messages;
    }
    function init() {

    }
}

class MessageStoreMultiple implements MessageStore
{

    function MessageStoreMultiple($stores){
        $this->stores = $stores;
    }

    function write($message)
    {
        foreach($this->stores as $store){
            $store->write($message);
        }
    }

    function readAll()
    {
        return $this->stores[0]->readAll();
    }

    function init() {
        foreach($this->stores as $store){
            $store->init();
        }
    }

}
