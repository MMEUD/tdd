<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Cristina Condruz
 * Date: 4/25/13
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
require_once('SortStrategy.php');
class AlphabeticallySortStrategy extends SortStrategy{
  public function sortEntityArray($entities){
    ksort($entities);
    return $entities;
  }
}
