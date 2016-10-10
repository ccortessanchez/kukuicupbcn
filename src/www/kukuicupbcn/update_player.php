<?php
 
/*
 * Following code will update a player information
 * A player is identified by player id (id)
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['id']) && isset($_POST['name']) && isset($_POST['passwd']) && isset($_POST['points'])) {
 
    $id = $_POST['id'];
    $name = $_POST['name'];
    $passwd = $_POST['passwd'];
    $points = $_POST['points'];
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql update row with matched id
    $result = mysql_query("UPDATE players SET name = '$name', passwd = '$passwd', points = '$points' WHERE id = $id");
 
    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "player successfully updated.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
 
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>