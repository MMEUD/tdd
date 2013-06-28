<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Claudia Diaconescu
 * Date: 6/27/13
 * Time: 12:11 PM
 * To change this template use File | Settings | File Templates.
 */
  $appContext = $_SERVER['TW_APP_CONTEXT'];
  $docRoot = $_SERVER['TW_DOC_ROOT'];

  require_once "$docRoot$appContext/IWriter.php";
  require_once "$docRoot$appContext/FileWriter.php";
  require_once "$docRoot$appContext/Message.php";

class BothWriters implements IWriter {
  private $writers;

  function __construct() {
    $this->writers = array();
  }

  public function add($writer) {
    $this->writers[] = $writer;
  }

  /**
   * @param Message $message
   */
  function write($message) {
    foreach($this->writers as $writer) {
      $writer->write($message);
    }

  }

}