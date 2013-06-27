<?php
  /**
   * Created by JetBrains PhpStorm.
   * User: Claudia Diaconescu
   * Date: 6/27/13
   * Time: 10:31 AM
   * To change this template use File | Settings | File Templates.
   */
  $appContext = $_SERVER['CAS_APP_CONTEXT'];
  $docRoot = $_SERVER['CAS_DOC_ROOT'];

  require_once "$docRoot$appContext/testdesign/Message.php";
  require_once "$docRoot$appContext/testdesign/SenderSortStrategy.php";
  require_once "$docRoot$appContext/testdesign/SortedMessagesList.php";
  require_once "$docRoot$appContext/testdesign/TimestampSortStrategy.php";
  require_once "$docRoot$appContext/testdesign/BothWriters.php";
  require_once "$docRoot$appContext/testdesign/FileWriter.php";

  class TweetRepository {
    private static $instance = null;
    private $messageList = array(); // array of Message
    private $writers;

    private function __construct() {


    }

    public static function getInstance() {
      if (!self::$instance) {
        self::$instance = new self();
        self::$writers = new BothWriters();
        self::$writers->add(new FileWriter());
      }
      return self::$instance;
    }


    /**
     * @param Message $message
     */
    public function addMessage($message) {
      $this->messageList[$message->getTimestamp()] = $message;
      $this->writers->write($message); // write in database / file
    }


    public function getMessages() {
      return $this->messageList;
    }

    public function getMessagesByTimestamp() {
      $messages = $this->getMessages();

      // sort by timestamp
      $sortedList = new SortedMessagesList(new TimestampSortStrategy());
      return $sortedList->sort($messages);
    }

    public function getMessagedFilteredByUserId($userId) {
      $messages = $this->getMessages();

      // sort by timestamp
      $sortedList = new SortedMessagesList(new SenderSortStrategy($userId));
      return $sortedList->sort($messages);
    }

  }