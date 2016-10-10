<?php
 
/*
 * Following code will get single team details
 * A team is identified by team id (id)
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// check for post data
if (isset($_GET["id"])) {
    $id = $_GET['id'];
 
    // get a team from teams table
    $result = mysql_query("SELECT *FROM teams WHERE id = $id");
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $team = array();
            $team["id"] = $result["id"];
            $team["name"] = $result["name"];
            $team["points"] = $result["points"];
            $team["tournament_id"] = $result["tournament_id"];
            // success
            $response["success"] = 1;
 
            // user node
            $response["team"] = array();
 
            array_push($response["team"], $team);
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no team found
            $response["success"] = 0;
            $response["message"] = "No team found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no team found
        $response["success"] = 0;
        $response["message"] = "No team found";
 
        // echo no users JSON
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>