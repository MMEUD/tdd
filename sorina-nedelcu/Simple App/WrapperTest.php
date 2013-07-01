<?php
require_once 'Wrapper.php';

class WrapperTest extends PHPUnit_Framework_TestCase {

  private $wrapper;

  function setUp() {
    $this->wrapper = new Wrapper();
  }

  function testItShouldWrapAnEmptyString() {
    $this->assertEquals('', $this->wrapper->wrap(''));
  }


  function testItDoesNotWrapAShortEnoughWord() {
     $textToBeParsed = 'word';
     $maxLineLength = 5;
     $this->assertEquals($textToBeParsed, $this->wrapper->wrap($textToBeParsed, $maxLineLength));
  }

  function testItWrapsAWordLongerThanLineLength() {
     $textToBeParsed = 'alongword';
     $maxLineLength = 5;
     $this->assertEquals("along\nword", $this->wrapper->wrapImproved($textToBeParsed, $maxLineLength));
  }

  function testItWraps3WordsOn2Lines() {
     $textToBeParsed = 'word word word';
     $maxLineLength = 12;
     $this->assertEquals("word word\nword", $this->wrapper->wrapWithSpace($textToBeParsed, $maxLineLength));
  }

  function testItWraps2WordsAtBoundry() {
     $textToBeParsed = 'word word';
     $maxLineLength = 8;
     $this->assertEquals("word\nword", $this->wrapper->wrapWithSpaceImproved($textToBeParsed, $maxLineLength));
  }

}