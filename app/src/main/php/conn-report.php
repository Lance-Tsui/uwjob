<?php
$servername = "localhost";
$username = "u393875839_school";
$password = "19890604";
$dbname = "u393875839_uwconnect";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die($conn->connect_error);
}

$sql = "SELECT * FROM report";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        echo $row["r_id"]. "," . $row["s_id"] . "," . $row["p_id"] . "\n";
    }
}

$conn->close();
?>
