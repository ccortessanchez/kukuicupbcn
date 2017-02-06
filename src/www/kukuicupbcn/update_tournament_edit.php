<?php
 
/*
 * Following code will update a player information
 * A player is identified by player id (id)
 */
error_reporting(E_ALL ^ E_DEPRECATED);
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['id']) && isset($_POST['init_date']) && isset($_POST['finish_date'])) {
 
	$id = $_POST['id'];
    $init_date = $_POST['init_date'];
	$finish_date = $_POST['finish_date'];
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql update row with matched id
    $result = mysql_query("UPDATE tournament SET init_date = '$init_date', finish_date = '$finish_date' WHERE id = $id");
 
    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "tournament successfully updated.";
 
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