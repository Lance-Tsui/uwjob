<?php
$servername = "localhost";
$username = "u393875839_school";
$password = "5Demands";
$dbname = "u393875839_uwconnect";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die($conn->connect_error);
}

$sql = "SELECT * FROM reportinfo";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        echo $row["r_id"]. "," . $row["program_id"] . "," . $row["rating"] . "," . $row["report_date"] . "," . $row["comment"] . "," . $row["student_year"] . "," . $row["student_semester"] . "," . $row["student_workterm_number"] . "," . $row["salary"] . "\n";
    }
}

$conn->close();
?>
