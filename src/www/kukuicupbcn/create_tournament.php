<?php
 
/*
 * Following code will create a new tournament row
 * All player details are read from HTTP Post Request
 */
 
error_reporting(E_ALL ^ E_DEPRECATED);
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['init_date'])&& isset($_POST['finish_date'])&& isset($_POST['duration'])&& isset($_POST['name'])) {
 
    $init_date = $_POST['init_date'];
	$finish_date = $_POST['finish_date'];
	$duration = $_POST['duration'];
	$name = $_POST['name'];
 
    // include db connect class
    require_once'/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql inserting a new row
    $result = mysql_query("INSERT INTO tournament(init_date, finish_date, duration, name) VALUES('$init_date', '$finish_date', '$duration', '$name')");
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "tournament successfully created.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        // echoing JSON response
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