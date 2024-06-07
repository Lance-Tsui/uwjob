<?php
$servername = "localhost";
$username = "u393875839_school";
$password = "19890604";
$dbname = "u393875839_uwconnect";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die($conn->connect_error);
}

$sql = "SELECT * FROM company";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        echo $row["c_id"]. "," . $row["c_name"] . "\n";
    }
}

$conn->close();
?>
