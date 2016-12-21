<?php
 
/*
 * Following code will list all the teams
 */
 
 
error_reporting(E_ALL ^ E_DEPRECATED);
 
// array for JSON response
$response = array();

// include db connect class
require_once'/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// get all teams from teams table
$result = mysql_query("SELECT *FROM teams") or die(mysql_error());
 
// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // teams node
    $response["teams"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $team = array();
        $team["id"] = $row["id"];
        $team["name"] = $row["name"];
        $team["points"] = $row["points"];
        $team["tournament_id"] = $row["tournament_id"];
 
        // push single team into final response array
        array_push($response["teams"], $team);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response);
} else {
    // no teams found
    $response["success"] = 0;
    $response["message"] = "No teams found";
 
    // echo no users JSON
    echo json_encode($response);
}
?>