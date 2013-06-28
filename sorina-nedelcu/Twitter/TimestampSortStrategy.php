<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Claudia Diaconescu
 * Date: 6/27/13
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
  $appContext = $_SERVER['CAS_APP_CONTEXT'];
  $docRoot = $_SERVER['CAS_DOC_ROOT'];


  require_once "$docRoot$appContext/testdesign/ISortStrategy.php";

  class TimestampSortStrategy implements ISortStrategy {
  /**
   * @param $list array of Message s
   */
  public function sort($list) {
    krsort($list);
    return $list;
  }


}