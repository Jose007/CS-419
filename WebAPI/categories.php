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

if(isset($_GET['id']) && (count($_GET) == 1)){

    if ( ! (is_numeric($_GET['id'])) 
            OR ( ! (stristr($_GET['id'], '.') === FALSE))
            OR ( ! (stristr($_GET['id'], ',') === FALSE))
        ) {

            exit();
        }

    //Prepare a statement
    if(!($stmt = $mysqli->prepare("SELECT id, name FROM categories WHERE id=?"))){
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
    if(!($stmt = $mysqli->prepare("SELECT c.id, c.name FROM contents c INNER JOIN categories cat ON c.cat_id = cat.id WHERE cat.id=? ORDER BY c.name"))){
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

    $item_id;
    $item_name;

    if(!($stmt->bind_result($item_id, $item_name))){
      echo "<p>Binding output parameters failed: (" . $stmt->errno . ") " . $stmt->error;
    }

    //Fetch results        
    $json_string = "{\"category-id\": \"" . $id ."\", \"category-name\": \"" . $name . "\", \"items\": [";

    while($stmt->fetch()){              
        $json_string = $json_string . "{\"id\": \"" . $item_id ."\", \"name\": \"" . $item_name . "\", \"item_url\": \"" . $url . "/items.php?id=" . $item_id . "\"}, ";
    }

    $json_string = rtrim($json_string, ", ");  //strip end comma
    $json_string = $json_string . "]}";

    echo $json_string;

    //Close statement
    if(!($stmt->close())){
      echo "<p>Close failed: (" . $stmt->errno . ") " . $stmt->error;
    }
}
else{
    //Prepare a statement
    if(!($stmt = $mysqli->prepare("SELECT id, name FROM categories ORDER BY name"))){
        echo "<p>Prepare failed: (" . $mysqli->errno . ") " . $mysqli->error;
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
    $json_string = "{\"categories\": [";
    while($stmt->fetch()){              
        $json_string = $json_string . "{\"id\": \"" . $id ."\", \"name\": \"" . $name . "\", \"category_url\": \"" . $url . "/categories.php?id=" . $id . "\"}, ";
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
if(!($mysqli->close())){
    echo "<p>Close failed: (" . $stmt->errno . ") " . $stmt->error;
}


