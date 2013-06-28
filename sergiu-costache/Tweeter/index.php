<?php
require 'init.php';

//var_dump($_POST);
if (isset($_GET['messageText'])) {
    if ($validator->isValid($_GET['messageText'])) {
	    $user = new User($_GET['author'], $_GET['author_handler']);
        $tweet = new tweet($_GET['messageText']);
        $tweet->setTimestamp(date("Y-m-d H:i:s"));
        $tweet->setAuthor($user);

        $storage->addMessage($tweet);
    } else {
        $errorMessages[] = 'Invalid message !';
    }
}
$messages = $storage->getMessages();
krsort($messages);

if (isset($_GET['filter']) && $_GET['filter'] != 'all') {
	$filter = new Filter($_GET['filter']);
	$messages = $filter->getFilteredMessages($messages);
}

?>
<body onload="document.getElementById('messageText').focus();">
<form name="msgTweet" id="msgTweet" method="GET">
	<input type="hidden" name="author_handler" id="author_handler" value="mosaic" />
	<select name="author" id="author" onchange="document.getElementById('author_handler').value = this.value;">
		<option value='mosaic'>mosaic works</option>
		<option value='mood'>mood media</option>
		<option value='noisivoi'>noi si voi</option>
	</select>
	<input type="text" name="messageText" id="messageText" value="" maxlength="5">
	<input type="submit" value="post" />
</form>

<form name="filterForm" id="filterForm" method="GET">
	<select name="filter" id="filter" onchange="this.form.submit();">
		<option value='all' <?if ($_GET['filter'] == 'all') echo 'selected="selected"';?>>all</option>
		<option value='mosaic' <?if ($_GET['filter'] == 'mosaic') echo 'selected="selected"';?>>mosaic works</option>
		<option value='mood' <?if ($_GET['filter'] == 'mood') echo 'selected="selected"';?>>mood media</option>
		<option value='noisivoi' <?if ($_GET['filter'] == 'noisivoi') echo 'selected="selected"';?>>noi si voi</option>
	</select>
	<input type="submit" value="filter" />
</form>


<? if (count($errorMessages) > 0) {?>
<div style="width: 500px; height: 13px; border: 1px solid red; margin:1px; font: normal 10px verdana; color:red;">
    <?
        foreach ($errorMessages as $message) {
            echo $message . '<br />';
        }
    ?>
</div>
<?}?>

<?
foreach ($messages as $msg) {
    $msg = unserialize($msg);
    ?>
    <div style="width: 500px; height: 13px; border: 1px solid #ccc; margin:1px; font: normal 10px verdana;">
        @<?=$msg->getAuthor()->getHandler();?>[<?=$msg->getAuthor()->getName();?>]
        &nbsp;
        <?=$msg->getTimestamp();?>
        &nbsp;
        <?=$msg->getMessage();?>
    </div>
    <?
}
?>
</body>