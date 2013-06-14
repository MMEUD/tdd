<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Sorina Nedelcu
 * Date: 5/23/13
 * Time: 1:58 PM
 * To change this template use File | Settings | File Templates.
 */
class Wrapper {

  function wrap($text) {
     return $text;
  }

  function wrapImproved($text, $lineLength) {
     if (strlen($text) > $lineLength)
        return substr ($text, 0, $lineLength) . "\n" . substr ($text, $lineLength);
     return $text;
  }

  function wrapWithSpace($text, $lineLength) {
     if (strlen($text) <= $lineLength)
        return $text;
     if (strpos(substr($text, 0, $lineLength), ' ') != 0)
        return substr ($text, 0, strrpos($text, ' ')) . "\n" . $this->wrap(substr($text, strrpos($text, ' ') + 1), $lineLength);
     return substr ($text, 0, $lineLength) . "\n" . $this->wrap(substr($text, $lineLength), $lineLength);
  }

  function wrapWithSpaceImproved($text, $lineLength) {
     $text = trim($text);
     if (strlen($text) <= $lineLength)
        return $text;
     if (strpos(substr($text, 0, $lineLength), ' ') != 0)
        return substr ($text, 0, strrpos($text, ' ')) . "\n" . $this->wrap(substr($text, strrpos($text, ' ') + 1), $lineLength);
     return substr ($text, 0, $lineLength) . "\n" . $this->wrap(substr($text, $lineLength), $lineLength);
  }
}
