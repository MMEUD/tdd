<?php
  /**
   * Created by JetBrains PhpStorm.
   * User: Claudia Diaconescu
   * Date: 6/27/13
   * Time: 10:31 AM
   * To change this template use File | Settings | File Templates.
   */
  $appContext = $_SERVER['EX_APP_CONTEXT'];
  $docRoot = $_SERVER['EX_DOC_ROOT'];

  require_once "$docRoot$appContext/Twitter/Message.php";
  require_once "$docRoot$appContext/Twitter/SenderSortStrategy.php";
  require_once "$docRoot$appContext/Twitter/SortedMessagesList.php";
  require_once "$docRoot$appContext/Twitter/TimestampSortStrategy.php";
  require_once "$docRoot$appContext/Twitter/BothWriters.php";
  require_once "$docRoot$appContext/Twitter/FileWriter.php";

  class TweetRepository {
    private $messageList = array(); // array of Message
    private $writers;

    public function __construct() {
      $this->writers = new BothWriters();
      $this->writers->add(new FileWriter());
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