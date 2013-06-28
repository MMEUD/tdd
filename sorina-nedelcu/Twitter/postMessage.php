<? /* Cassiopeia project file */ ?>
<?
$appContext = $_SERVER['CAS_APP_CONTEXT'];
$docRoot = $_SERVER['CAS_DOC_ROOT'];

require_once "$docRoot$appContext/testdesign/Message.php";
require_once "$docRoot$appContext/testdesign/User.php";
require_once "$docRoot$appContext/testdesign/TweetCommand.php";
require_once "$docRoot$appContext/testdesign/BothWriters.php";
require_once "$docRoot$appContext/testdesign/SenderSortStrategy.php";
require_once "$docRoot$appContext/testdesign/SortedMessagesList.php";
require_once "$docRoot$appContext/testdesign/TimestampSortStrategy.php";

?>
<html>
<head>

  <title>..:TWEET Message</title>
  <LINK REL="stylesheet" HREF="style/css/global.css">
  <script defer type="text/javascript" src="js/pngfix.js"></script>
  <link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico"/>
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
</head>

<?
if (session_id() == "") session_start();

if (isset($_SESSION['tweet'])) {
  $tweetRepository = $_SESSION["tweet"];
}

$tweetCommand = new TweetCommand($tweetRepository);
$tweetCommand->execute();

$userId = isset($_REQUEST["idUser"]) ? $_REQUEST["idUser"] : "";

if($userId == "all") {
  $messages = $tweetRepository->getMessagesByTimestamp();
} else {
  $messages = $tweetRepository->getMessagedFilteredByUserId($userId);
}

?>



<body onload="">
<div align="center" id="centered-vertical">
  <div id="login">
    <br>


    <form name="frmTweet" action="postMessage.php" method="post">
      <div style="margin:30px 0 0 100px;">
        <p align="left">

          <label class="small-yellow">Select user:</label>
          <select name="idUser" id="idUser" onchange="submit();">
            <option value="all" id="all" <?=$userId == "all" ? "selected":""?>>All</option>
            <option value="claudiad" id="claudiad" <?=$userId == "claudiad" ? "selected" : ""?>>Claudia Diaconescu</option>
            <option value="sorinan" id="sorinan" <?=$userId == "sorinan" ? "selected" : ""?>>Sorina Nedelcu</option>
            <option value="cristinac" id="cristinac" <?=$userId == "cristinac" ? "selected" : ""?>>Cristina Condruz</option>
          </select>

          <label class="small-yellow">Message List:</label>
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


</body>
</html>