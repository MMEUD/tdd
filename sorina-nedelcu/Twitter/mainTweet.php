<? /* Cassiopeia project file */ ?>
<?
$appContext = $_SERVER['EX_APP_CONTEXT'];
$docRoot = $_SERVER['EX_DOC_ROOT'];

require_once "$docRoot$appContext/Twitter/Message.php";
require_once "$docRoot$appContext/Twitter/TweetRepository.php";


?>
<html>
<head>

  <title>..:TWEET</title>

  <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
</head>


<?

if (session_id() == "") session_start();

if (!isset($_SESSION['tweet'])) {
  $_SESSION["tweet"] = new TweetRepository();
}

//$tweetTxt = isset($_REQUEST["tweetTxt"]) ? $_REQUEST["tweetTxt"] : "";
//$tweetMessages->addMessage(new Message($tweetTxt));

?>

<body onload="">
<div align="center" id="centered-vertical">
  <div id="login">
    <br>


      <form name="frmTweet" action="postMessage.php" method="post">
        <div style="margin:30px 0 0 100px;">
          <p align="left">
            <label class="small-yellow">Select user:</label>
            <select name="idUser" id="idUser">
              <option value="claudiad" id="claudiad" selected>Claudia Diaconescu</option>
              <option value="sorinan" id="sorinan">Sorina Nedelcu</option>
              <option value="cristinac" id="cristinac">Cristina Condruz</option>
            </select>
            <br>

            <label class="small-yellow">Enter text:</label>
            <input type="text" id="tweetTxt" name="tweetTxt" class="inputbox" maxlength="140" size="100">
            <br>
            <input type="submit" src="" name="Tweet" value="Tweet" style="vertical-align:top;"
                   onClick="">
          </p>



        </div>
      </form>
    </div>

  </div>
</div>
<?

?>

</body>
</html>