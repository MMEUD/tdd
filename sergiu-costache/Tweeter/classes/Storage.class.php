<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Administrator
 * Date: 27.06.2013
 * Time: 15:30
 * To change this template use File | Settings | File Templates.
 */
abstract class Storage {
    abstract function addMessage($tweet);
    abstract function getMessages();

}

class SessionStorage extends Storage {

    function SessionStorage() {
        if (! isset($_SESSION['msg'])) $_SESSION['msg'] = array();
    }

    function addMessage($tweet) {
        $_SESSION['msg'][] = serialize($tweet);
    }

    function getMessages() {
        return $_SESSION['msg'];
    }
}
class FileStorage extends Storage {

    function FileStorage() {}

    function addMessage($tweet) {
        global $config;

        $fh = fopen($config['FILE'], 'a');
        fwrite($fh, serialize($tweet) . "\n");
        fclose($fh);
    }

    function getMessages() {
        global $config;

        $messages = file($config['FILE']);
        return $messages;
    }
}

class MultipleStorage extends Storage {

    private $storages;

    function MultipleStorage() {
        $this->storages = array();
    }

    function addStorage($storage){
        $this->storages[] = $storage;
    }

    function addMessage($tweet) {
        foreach($this->storages as $storage){
            $storage->addMessage($tweet);
        }
    }

    function getMessages() {
        return $this->storages[0]->getMessages();
    }
}