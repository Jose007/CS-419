<?php
header('Content-type: text/html; charset=utf-8');
ini_set('display_errors',1);
error_reporting(E_ALL);

include './config.php';

/*session_start();

//if session is not active, kick back to login.html
if(!isset($_SESSION['active'])){
    $filePath = explode('/', $_SERVER['PHP_SELF'], -1);
    $filePath = implode('/', $filePath);
    $redirect = "http://" . $_SERVER['HTTP_HOST'] . $filePath;
    header("Location:{$redirect}/login.html", true); 
}*/

//Create connection with database.
$mysqli = new mysqli($host, 
                    $username, 
                    $password, 
                    $database
);

if ($mysqli->connect_errno) {
    echo "<p>Failed to connect to MySQL: (" . $mysqli->connect_errno
        . ") " . $mysqli->connect_error;
}

//url to be used for in returned json
$filePath = explode('/', $_SERVER['PHP_SELF'], -1);
$filePath = implode('/', $filePath);
$url = "http://" . $_SERVER['HTTP_HOST'] . $filePath;

//Prepare a statement
if(!($stmt = $mysqli->prepare("SELECT id, name, street, city, st, zip FROM companies"))) 
{
  echo "<p>Prepare failed: (" . $mysqli->errno . ") " . $mysqli->error;
}           

//Execute statement
if(!($stmt->execute())){ 
  echo "<p>Execute failed: (" . $stmt->errno . ") " . $stmt->error; 
}

$bus_id;
$bus_name;
$street;
$city;
$state;
$zip;

if(!($stmt->bind_result($bus_id, $bus_name, $street, $city, $state, $zip))) {
    echo "<p>Binding output parameters failed: (" . $stmt->errno . ") " . $stmt->error;
}

//Fetch results        
$json_string = "{\"addresses\": [";

while($stmt->fetch()) {    

    if ( ! ($city === ""))
    {
        $city = $city . ", ";
    }
    if ($zip == null)
    {
        $zip = "";
    }
    

    $json_string = $json_string . "{\"id\": \"" . $bus_id ."\", \"name\": \"" . $bus_name . "\", \"address\": \"" . $street . " " . $city . $state . " " . $zip . "\"}, ";   
}


$json_string = rtrim($json_string, ", ");  //strip end comma
$json_string = $json_string . "]}";

echo $json_string;

//Close statement
if(!($stmt->close())){
  echo "<p>Close failed: (" . $stmt->errno . ") " . $stmt->error;
}





