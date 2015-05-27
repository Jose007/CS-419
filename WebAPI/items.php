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

if(isset($_GET['id']) && (count($_GET) == 1)) {

    if ( ! (is_numeric($_GET['id'])) 
            OR ( ! (stristr($_GET['id'], '.') === FALSE))
            OR ( ! (stristr($_GET['id'], ',') === FALSE))
        ) {

            exit();
        }
  
    //Prepare a statement
    if(!($stmt = $mysqli->prepare("SELECT id, name FROM contents WHERE id=?"))){
        echo "<p>Prepare failed: (" . $mysqli->errno . ") " . $mysqli->error;
    }             

    //Bind variables
    if(!($stmt->bind_param("i", $_GET['id']))){
      echo "<p>Binding parameters failed: (" . $stmt->errno . ") " . $stmt->error;
    }

    //Execute statement
    if(!($stmt->execute())){ 
      echo "<p>Execute failed: (" . $stmt->errno . ") " . $stmt->error; 
    }

    $id;
    $name;

    if(!($stmt->bind_result($id, $name))){
        echo "<p>Binding output parameters failed: (" . $stmt->errno . ") " . $stmt->error;
    }

    //Fetch results        
    if(!($stmt->fetch())) { 
        exit();             
    }

    //Close statement
    if(!($stmt->close())){
        echo "<p>Close failed: (" . $stmt->errno . ") " . $stmt->error;
    }


    //Prepare a statement
    if(!($stmt = $mysqli->prepare("SELECT com.id, com.name, com.info, com.phone, com.website, com.street, com.city, com.st, com.zip, cc.reuse, cc.repair 
                                    FROM companies com 
                                    INNER JOIN company_content cc 
                                    ON cc.company_id = com.id
                                    WHERE cc.content_id=?"))) 
    {
      echo "<p>Prepare failed: (" . $mysqli->errno . ") " . $mysqli->error;
    }           

    //Bind variables
    if(!($stmt->bind_param("i", $id))){
      echo "<p>Binding parameters failed: (" . $stmt->errno . ") " . $stmt->error;
    }

    //Execute statement
    if(!($stmt->execute())){ 
      echo "<p>Execute failed: (" . $stmt->errno . ") " . $stmt->error; 
    }

    $bus_id;
    $bus_name;
    $info;
    $phone;
    $website;
    $street;
    $city;
    $state;
    $zip;
    $reuse;
    $repair;

    if(!($stmt->bind_result($bus_id, $bus_name, $info, $phone, $website, $street, $city, $state, $zip, $reuse, $repair))) {
        echo "<p>Binding output parameters failed: (" . $stmt->errno . ") " . $stmt->error;
    }

    //Fetch results        
    $json_string = "{\"item-id\": \"" . $id ."\", \"item-name\": \"" . $name . "\", \"businesses\": [";

    while($stmt->fetch()) {    
        //only include business that have a marked reuse or repair or both as 1.
        if(($reuse == 1) OR ($repair == 1)){
            if ( ! ($city === ""))
            {
                $city = $city . ", ";
            }
            if ($zip == null)
            {
                $zip = "";
            }
            

            $json_string = $json_string . "{\"id\": \"" . $bus_id ."\", \"name\": \"" . $bus_name . "\", \"address\": \"" . $street . " " . $city . $state . " " . $zip 
                . "\", \"phone\": \"" . $phone . "\", \"website\": \"" . $website . "\", \"info\": \"" . $info . "\", \"application\": {\"reuse\": \"" . $reuse . "\", \"repair\": \"" . $repair . "\"}}, ";   
        }
    }

    $json_string = rtrim($json_string, ", ");  //strip end comma
    $json_string = $json_string . "]}";

    echo $json_string;

    //Close statement
    if(!($stmt->close())){
      echo "<p>Close failed: (" . $stmt->errno . ") " . $stmt->error;
    }

}

//Close connection
if(!($mysqli->close())) {
    echo "<p>Close failed: (" . $stmt->errno . ") " . $stmt->error;
}
?>