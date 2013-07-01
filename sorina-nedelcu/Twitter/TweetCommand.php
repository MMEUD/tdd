<?php
  /**
   * Created by JetBrains PhpStorm.
   * User: Claudia Diaconescu
   * Date: 6/27/13
   * Time: 12:11 PM
   * To change this template use File | Settings | File Templates.
   */
  $appContext = $_SERVER['EX_APP_CONTEXT'];
  $docRoot = $_SERVER['EX_DOC_ROOT'];

  require_once "$docRoot$appContext/Twitter/ICommand.php";
  require_once "$docRoot$appContext/Twitter/Message.php";
   require_once "$docRoot$appContext/Twitter/TweetRepository.php";

  class TweetCommand implements ICommand {


    private $tweetRepository;

    /**
     * @param TweetRepository $tweetRepository
     */
    function __construct($tweetRepository) {

      $this->tweetRepository = $tweetRepository;
    }

    function execute() {
      $tweetTxt = isset($_REQUEST["tweetTxt"]) ? $_REQUEST["tweetTxt"] : "";
      $userId = isset($_REQUEST["idUser"]) ? $_REQUEST["idUser"] : "";


      $message = new Message($tweetTxt);
      $message->setFromUser(new User($userId, "Claudia", "Diaco"));

      if ($message->validateLength()) {
        $this->tweetRepository->addMessage($message); // add in memory
     }
    }

  }