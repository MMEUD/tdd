<? /* Cassiopeia project file */ ?>
<?
$appContext = $_SERVER['TW_APP_CONTEXT'];
$docRoot = $_SERVER['TW_DOC_ROOT'];

require_once "$docRoot$appContext/Message.php";
require_once "$docRoot$appContext/User.php";
require_once "$docRoot$appContext/TweetCommand.php";
require_once "$docRoot$appContext/BothWriters.php";
require_once "$docRoot$appContext/SenderSortStrategy.php";
require_once "$docRoot$appContext/SortedMessagesList.php";
require_once "$docRoot$appContext/TimestampSortStrategy.php";

?>

<?
if (isset($_SESSION['tweet'])) {
  $tweetRepository = unserialize($_SESSION["tweet"]);
}
if(isset($_REQUEST["command"]) && ($_REQUEST["command"] == "twitterCommand" || $_REQUEST["command"] == "")) {
  $tweetTxt = isset($_REQUEST["tweetTxt"]) ? $_REQUEST["tweetTxt"] : "";
  $userId = isset($_REQUEST["idUser"]) ? $_REQUEST["idUser"] : "";
  $userIdToView = isset($_REQUEST["idUserToView"]) ? $_REQUEST["idUserToView"] : "";

  $tweetCommand = new TweetCommand($tweetRepository, $tweetTxt, $userId);
  $tweetCommand->execute();

  // updates the repository on the session
  $_SESSION["tweet"] = serialize($tweetRepository);

  if ($userIdToView == "all" || $userIdToView == "") {
    $messages = $tweetRepository->getMessagesByTimestamp();
  }
  else {
    $messages = $tweetRepository->getMessagedFilteredByUserId($userIdToView);
  }
?>

<div align="center" id="centered-vertical">
  <div id="login">
    <br>


    <form name="frmTweet" action="mainTweet.php" method="post">
      <div style="margin:30px 0 0 100px;">
        <p align="left">

          <label class="small-yellow">Message List:</label>
          <select name="idUserToView" id="idUserToView" onchange="submit();">
            <option value="all" id="all" <?=$userIdToView == "all" ? "selected":""?>>All</option>
            <option value="claudiad" id="claudiad" <?=$userIdToView == "claudiad" ? "selected" : ""?>>Claudia Diaconescu</option>
            <option value="sorinan" id="sorinan" <?=$userIdToView == "sorinan" ? "selected" : ""?>>Sorina Nedelcu</option>
            <option value="cristinac" id="cristinac" <?=$userIdToView == "cristinac" ? "selected" : ""?>>Cristina Condruz</option>
          </select>
          <br>
          <table>
          <?
          foreach($messages as $message) {
          ?>
          <tr>
            <td><?=$message->getFromMessage();?>
            </td>
          </tr>
          <?
          }
          ?>
          </table>
        </p>
      </div>
    </form>
  </div>

</div>
</div>

<?}?>
