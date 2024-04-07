<?php
$servername = "localhost";
$username = "u393875839_school";
$password = "5Demands";
$dbname = "u393875839_uwconnect";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die($conn->connect_error);
}

$sql = "SELECT * FROM studentpersonalinfo";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        echo $row["s_id"]. "," . $row["student_name"] . "," . $row["gender"] . "," . $row["email"] . "," . $row["pwd"] . "\n";
    }
}

$conn->close();
?>
