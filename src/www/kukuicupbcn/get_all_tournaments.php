<?php
 
/*
 * Following code will list all the tournaments
 */
 
 
error_reporting(E_ALL ^ E_DEPRECATED);
 
// array for JSON response
$response = array();

// include db connect class
require_once'/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// get all teams from tournaments table
$result = mysql_query("SELECT *FROM tournament") or die(mysql_error());
 
// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // tournament node
    $response["tournaments"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $tournament = array();
        $tournament["id"] = $row["id"];
        $tournament["init_date"] = $row["init_date"];
        $tournament["finish_date"] = $row["finish_date"];
        //$tournament["duration"] = $row["duration"];
 
        // push single team into final response array
        array_push($response["tournaments"], $tournament);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response);
} else {
    // no teams found
    $response["success"] = 0;
    $response["message"] = "No tournaments found";
 
    // echo no users JSON
    echo json_encode($response);
}
?>