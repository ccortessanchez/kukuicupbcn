<?php
 
/*
 * Following code will list all activity register
 */
 
 
error_reporting(E_ALL ^ E_DEPRECATED);
 
// array for JSON response
$response = array();

// include db connect class
require_once'/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// get all teams from teams table
$result = mysql_query("SELECT *FROM activity_reg") or die(mysql_error());
 
// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // teams node
    $response["activity_reg"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $activity_reg = array();
        $activity_reg["player_name"] = $row["player_name"];
		$activity_reg["activity_name"] = $row["activity_name"];
 
        // push single team into final response array
        array_push($response["activity_reg"], $activity_reg);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response);
} else {
    // no teams found
    $response["success"] = 0;
    $response["message"] = "No register found";
 
    // echo no users JSON
    echo json_encode($response);
}
?>