<?php
  /**
   * Created by JetBrains PhpStorm.
   * User: Claudia Diaconescu
   * Date: 6/27/13
   * Time: 3:08 PM
   * To change this template use File | Settings | File Templates.
   */
  $appContext = $_SERVER['EX_APP_CONTEXT'];
  $docRoot = $_SERVER['EX_DOC_ROOT'];


  require_once "$docRoot$appContext/Twitter/ISortStrategy.php";

  class SortedMessagesList implements ISortStrategy{
    private $strategy;

    function __construct($strategy) {
      $this->strategy = $strategy;
    }

    public function sort($list) {
      return $this->strategy->sort($list);
    }

  }