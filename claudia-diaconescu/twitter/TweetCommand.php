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

  require_once "$docRoot$appContext/ICommand.php";
  require_once "$docRoot$appContext/Message.php";
   require_once "$docRoot$appContext/TweetRepository.php";

  class TweetCommand implements ICommand {


    private $tweetRepository;
    private $text;
    private $userId;


    /**
     * @param TweetRepository $tweetRepository
     * @param $text
     * @param $userId
     */
    function __construct($tweetRepository, $text, $userId) {
      $this->tweetRepository = $tweetRepository;
      $this->text = $text;
      $this->userId = $userId;
    }

    function execute() {
      $message = new Message($this->text);
      $message->setFromUser(new User($this->userId, "Claudia", "Diaconescu"));

      if ($message->validateLength()) {
        $this->tweetRepository->addMessage($message); // add in memory
     }
    }

  }