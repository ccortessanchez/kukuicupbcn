<?php
 
/*
 * Following code will update a team information
 * A team is identified by team id (id)
 */
error_reporting(E_ALL ^ E_DEPRECATED); 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['id']) && isset($_POST['name']) && isset($_POST['points'])) {
 
    $id = $_POST['id'];
    $name = $_POST['name'];
    $points = $_POST['points'];
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql update row with matched id
    $result = mysql_query("UPDATE teams SET name = '$name', points = '$points' WHERE id = $id");
 
    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "team successfully updated.";
 
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