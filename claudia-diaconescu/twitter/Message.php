<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Claudia Diaconescu
 * Date: 6/27/13
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */

class Message {
  private $message;
  private $fromUser;// the one initiating the tweet
  private $toUser; // the one initiating the tweet
  private $timestamp;

  function __construct($message) {
    $this->message = $message;
    $dateTime = new DateTime();
    $this->timestamp = $dateTime->getTimestamp();
  }


  public function getFromMessage() {
    return $this->timestamp ." From: " .$this->fromUser->getId() . " ". $this->message;
  }

  public function validateLength() {
    return strlen($this->message) >0 && strlen($this->message) < 140;
  }

  public function setFromUser($fromUser) {
    $this->fromUser = $fromUser;
  }

  public function getFromUser() {
    return $this->fromUser;
  }

  public function setToUser($toUser) {
    $this->toUser = $toUser;
  }

  public function getToUser() {
    return $this->toUser;
  }

  public function getTimestamp() {
    return $this->timestamp;
  }


}