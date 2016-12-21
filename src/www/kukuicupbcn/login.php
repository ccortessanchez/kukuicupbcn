<?php

error_reporting(E_ALL ^ E_DEPRECATED);
// json response array
$response = array();

 // include db connect class
require_once 'db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();

if (isset($_POST['name']) && isset($_POST['passwd'])) {
 
    // receiving the post params
    $name = $_POST['name'];
    $passwd = $_POST['passwd'];
		
	// include db connect class
    //require_once __DIR__ . '/db_connect.php';
	
	// connecting to db
    //$db = new DB_CONNECT();
	
	$result = mysql_query("SELECT * FROM players WHERE name LIKE '$name' AND passwd LIKE '$passwd'");
	
	//fetching result
	$check = mysql_fetch_array($result);
	
	//if we got some result
	if(isset($check)){
	//displaying success
	echo "success";
	}else{
	//displaying failure
	echo "failure";
	}
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>