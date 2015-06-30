<?php
$name=$_POST['name'];
$emailadr=$_POST['email'];
$msg=$_POST['message'];

$email = "info@vremind.com";
$to=$email;
$sub='Contact Form';
$body="Hello,<br>I am "+$name." <br>".$msg;
$header='From: vRemind.com <'.$emailadr.'>';
if(mail($to,$sub,$body,$header))
{
echo 'sent';

}
else { 

echo 'no';
}
?>