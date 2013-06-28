<? /* Cassiopeia project file */ ?>
<?
$appContext = $_SERVER['TW_APP_CONTEXT'];
$docRoot = $_SERVER['TW_DOC_ROOT'];

require_once "$docRoot$appContext/Message.php";
require_once "$docRoot$appContext/TweetRepository.php";


?>
<html>
<head>
  <title>..:TWITTER</title>
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
</head>


<?

if (session_id() == "") session_start();

if (!isset($_SESSION['tweet'])) {
  $_SESSION["tweet"] = serialize(new TweetRepository());
}

?>

<body onload="">
<div align="center" id="centered-vertical">
  <div id="login">
    <br>


      <form name="frmTweet" action="mainTweet.php" method="post">
        <div style="margin:30px 0 0 100px;">
          <p align="left">
            <label class="small-yellow">User sending the message:</label>
            <select name="idUser" id="idUser">
              <option value="claudiad" id="claudiad" selected>Claudia Diaconescu</option>
              <option value="sorinan" id="sorinan">Sorina Nedelcu</option>
              <option value="cristinac" id="cristinac">Cristina Condruz</option>
            </select>
            <br>

            <label class="small-yellow">Enter text:&nbsp;&nbsp;&nbsp;&nbsp;</label>
            <input type="text" id="tweetTxt" name="tweetTxt" class="inputbox" maxlength="140" size="100">
            <br>
            <input type="hidden" name="command" id="command"/>
            <input type="submit" src="" name="Tweet" value="Tweet" style="vertical-align:top;"
                   onClick="document.frmTweet.command.value = 'twitterCommand';">
          </p>
<?
          require_once "$docRoot$appContext/postMessage.php";
?>
        </div>
      </form>
    </div>

  </div>
</div>
<?

?>

</body>
</html>