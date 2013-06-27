<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Claudia Diaconescu
 * Date: 6/27/13
 * Time: 11:35 AM
 * To change this template use File | Settings | File Templates.
 */
  $appContext = $_SERVER['CAS_APP_CONTEXT'];
  $docRoot = $_SERVER['CAS_DOC_ROOT'];

  require_once "$docRoot$appContext/testdesign/IWriter.php";
  require_once "$docRoot$appContext/testdesign/Message.php";

  class FileWriter implements IWriter{
  private $fileHandler;

  function __construct() {
    try {
      // todo get path from config
      $this->fileHandler = fopen("d:\\tweetMessages.log", "a+");
    } catch (Exception $e) {
      echo $e->getMessage();
    }
  }

  /**
   * @param Message $message
   */
  function write($message) {
    $this->fileHandler = fopen("d:\\tweetMessages.log", "a+");
    fwrite($this->fileHandler, "\n" . $message->getFromMessage(). "\n\n");
    fclose($this->fileHandler);
  }

}