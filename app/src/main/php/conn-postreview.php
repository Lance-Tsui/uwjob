<?php

$servername = "localhost";
$username = "u393875839_school";
$password = "5Demands";
$dbname = "u393875839_uwconnect";

$companyName = $_POST['company'];
$positionName = $_POST['position'];
$review = $_POST['review'];
$rating = floatval($_POST['rating']);

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die($conn->connect_error);
}

$sql = "SELECT c_id FROM company WHERE c_name = '$companyName'";
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $companyId = $row['c_id'];
} else {
    $sql = "INSERT INTO company (c_name) VALUES ('$companyName')";
    if ($conn->query($sql) === TRUE) {
        $companyId = $conn->insert_id;
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
        exit();
    }
}

$sql = "SELECT p_id FROM position WHERE position_name = '$positionName' AND c_id = $companyId";
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $positionId = $row['p_id'];
} else {
    $sql = "INSERT INTO position (position_name, c_id) VALUES ('$positionName', $companyId)";
    if ($conn->query($sql) === TRUE) {
        $positionId = $conn->insert_id;
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
        exit();
    }
}

$sql = "INSERT INTO report (s_id, p_id) VALUES (327, $positionId)";
if ($conn->query($sql) === TRUE) {
    $reportId = $conn->insert_id;
    $sql = "INSERT INTO reportinfo (r_id, program_id, rating, report_date, comment, student_year, student_semester, student_workterm_number, salary) VALUES ($reportId, 27, $rating, '2024-04-01', '$review', 0, 0, 0, 0)";
    $result = $conn->query($sql);
} else {
    echo $conn->error;
}

$conn->close();
?>