<?php
 
/*
 * Following code will get single player details
 * A player is identified by player id (id)
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once 'db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// check for post data
if (isset($_GET["name"])) {
    $name = $_GET['name'];
 
    // get a player from players table
    $result = mysql_query("SELECT *FROM players WHERE name LIKE $name");
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $player = array();
            $player["id"] = $result["id"];
            $player["name"] = $result["name"];
            $player["passwd"] = $result["passwd"];
            $player["points"] = $result["points"];
            $player["team_id"] = $result["team_id"];
            // success
            $response["success"] = 1;
 
            // user node
            $response["player"] = array();
 
            array_push($response["player"], $player);
 
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