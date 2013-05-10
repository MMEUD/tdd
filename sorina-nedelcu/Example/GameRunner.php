<?php

include __DIR__.'/Game.php';

$notAWinner;

  $aGame = new Game();
  
  $aGame->addPlayerAndInitPlacesAndInitPursesAndInitPenaltyBoxAndShowNameInfoAndShowPlayerNumber("Chet");
  $aGame->addPlayerAndInitPlacesAndInitPursesAndInitPenaltyBoxAndShowNameInfoAndShowPlayerNumber("Pat");
  $aGame->addPlayerAndInitPlacesAndInitPursesAndInitPenaltyBoxAndShowNameInfoAndShowPlayerNumber("Sue");
  
  
  do {
    
    $aGame->roll(rand(0,5) + 1);
    
    if (rand(0,9) == 7) {
      $notAWinner = $aGame->wrongAnswer();
    } else {
      $notAWinner = $aGame->wasCorrectlyAnswered();
    }
    
    
    
  } while ($notAWinner);
  
