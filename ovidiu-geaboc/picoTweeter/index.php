<?php

require './classes/Message.class.php';
require './classes/MessageStore.class.php';
require './classes/MessageWall.class.php';

define('SORT_BY_DATE_DESC',1);
define('SORT_BY_DATE_ASC',2);

$messageStore = new MessageStoreMemory();
$wall = new MessageWall($messageStore);

if(isset($_POST['inputMessageText'])){

    $messageText = $_POST['inputMessageText'];
    $authorName = $_POST['selectMessageAuthor'];

    $validator = new Validator;

    if($validator->isValid($m)) {
        $message =  new Message($messageText,$authorName);
        $messageStore->write($message);

    }
}
?>

<form action="" method="post" name="frmPostMessage" id="frmPostMessage">

    <input type="text" size=140 name="inputMessageText" value="">
    <input type="submit" value="Post">
    <select name="selectMessageAuthor">
        <option value="Anonymous" selected>Anonymous</option>
        <option value="John Smith">John Smith</option>
        <option value="Barack Obama">Barack Obama</option>
        <option value="Un baiat">Un baiat</option>
    </select>
</form>

<?php

$wall->show();

?>