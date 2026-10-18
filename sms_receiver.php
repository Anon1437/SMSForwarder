<?php
header('Content-Type: application/json');

$sender = $_POST['sender'] ?? 'Unknown';
$body = $_POST['body'] ?? '';
$time = date('Y-m-d H:i:s');

if (empty($body)) {
    echo json_encode(["status" => "error", "message" => "No body received"]);
    exit;
}

$logEntry = "[$time] Sender: $sender | Message: $body\n";
file_put_contents('sms_log.txt', $logEntry, FILE_APPEND | LOCK_EX);

echo json_encode(["status" => "success", "message" => "SMS saved"]);
?>
