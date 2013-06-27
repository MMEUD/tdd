<?php
  /**
   * Created by JetBrains PhpStorm.
   * User: Claudia Diaconescu
   * Date: 6/27/13
   * Time: 3:06 PM
   * To change this template use File | Settings | File Templates.
   */
  $appContext = $_SERVER['CAS_APP_CONTEXT'];
  $docRoot = $_SERVER['CAS_DOC_ROOT'];


  require_once "$docRoot$appContext/testdesign/ISortStrategy.php";

  class SenderSortStrategy implements ISortStrategy {
    private $userId;

    function __construct($userId) {
      $this->userId = $userId;
    }


    public function sort($list) {
      $sortedList = array();
      foreach ($list as $key => $message) {
        if ($message->getFromUser()->getId() == $this->userId) {
          $sortedList[$key] = $message;
        }
      }
      krsort($sortedList);
      return $sortedList;
    }


  }