<?php

$con = mysqli_connect("localhost","root","","photo");

$sql = 'INSERT INTO postphoto(url,user_id,type) VALUES("'.$_POST['url'].'"
,"'.$_POST['user_id'].'","'.$_POST['type'].'")';

$result = mysqli_query($con,$sql);
if($result){
  echo "Success";
}else{
  echo "Failed";
}



?>