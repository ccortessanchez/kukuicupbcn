<?php
 
/*
 * Following code will list all badges
 */
 
 
error_reporting(E_ALL ^ E_DEPRECATED);
 
// array for JSON response
$response = array();

// include db connect class
require_once'/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// get all teams from teams table
$result = mysql_query("SELECT *FROM players_badges") or die(mysql_error());
 
// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // teams node
    $response["players_badges"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $players_badges = array();
        $players_badges["player_id"] = $row["player_id"];
		$players_badges["badge_id"] = $row["badge_id"];
 
        // push single team into final response array
        array_push($response["players_badges"], $players_badges);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response);
} else {
    // no teams found
    $response["success"] = 0;
    $response["message"] = "No badges found";
 
    // echo no users JSON
    echo json_encode($response);
}
?>