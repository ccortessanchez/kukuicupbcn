<?php
 
/*
 * Following code will get single team details
 * A team is identified by team id (id)
 */
error_reporting(E_ALL ^ E_DEPRECATED); 
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
    $result = mysql_query("SELECT *FROM tournament WHERE id = $id");
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $tournament = array();
            $tournament["id"] = $row["id"];
			$tournament["init_date"] = $row["init_date"];
			$tournament["finish_date"] = $row["finish_date"];
			$tournament["duration"] = $row["duration"];
			$tournament["name"] = $row["name"];
            // success
            $response["success"] = 1;
 
            // user node
            $response["tournament"] = array();
 
            array_push($response["tournament"], $tournament);
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no team found
            $response["success"] = 0;
            $response["message"] = "No tournament found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no team found
        $response["success"] = 0;
        $response["message"] = "No tournament found";
 
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