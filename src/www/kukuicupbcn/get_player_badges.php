<?php
 
/*
 * Following code will get single player badges
 * A player is identified by player id (id)
 */
error_reporting(E_ALL ^ E_DEPRECATED);
// array for JSON response
$response = array();
 
// include db connect class
require_once 'db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// check for post data
if (isset($_GET["player_id"])) {
    $player_id = $_GET['player_id'];
 
    // get a player from players table
    $result = mysql_query("SELECT *FROM players_badges WHERE player_id = $player_id");
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $players_badges = array();
            $player_badges["badge_id"] = $result["badge_id"];
            // success
            $response["success"] = 1;
 
            // user node
            $response["players_badges"] = array();
 
            array_push($response["players_badges"], $player_id);
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no player found
            $response["success"] = 0;
            $response["message"] = "No player found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no player found
        $response["success"] = 0;
        $response["message"] = "No player found";
 
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