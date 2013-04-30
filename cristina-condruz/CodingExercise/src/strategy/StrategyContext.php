<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Cristina Condruz
 * Date: 4/26/13
 * Time: 5:54 PM
 * To change this template use File | Settings | File Templates.
 */
require_once('AlphabeticallySortStrategy.php');
class StrategyContext {
    private $strategy = NULL;
    //namespace(?) is not instantiated at construct time
    public function __construct($strategy_ind_id) {
        switch ($strategy_ind_id) {
            case "A":
                $this->strategy = new AlphabeticallySortStrategy();
            break;
            case "R":
                $this->strategy = new ReverseSortStrategy();
            break;
        }
    }
    public function getSortedArray($entities) {
      return $this->strategy->sortEntityArray($entities);
    }
}