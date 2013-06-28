<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Claudia Diaconescu
 * Date: 6/27/13
 * Time: 12:11 PM
 * To change this template use File | Settings | File Templates.
 */
  $appContext = $_SERVER['CAS_APP_CONTEXT'];
  $docRoot = $_SERVER['CAS_DOC_ROOT'];

  require_once "$docRoot$appContext/testdesign/IWriter.php";
  require_once "$docRoot$appContext/testdesign/FileWriter.php";
  require_once "$docRoot$appContext/testdesign/Message.php";

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