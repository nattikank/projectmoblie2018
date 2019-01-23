<?php

$con = mysqli_connect("localhost","root","","photo");

$sql = 'UPDATE user set Name="'.$_POST['name'].'",tel="'.$_POST['tel'].'",address="'.$_POST['address'].'"where id="'.$_POST['id'].'"';

$result = mysqli_query($con,$sql);

if($result){
	echo "Success";
}else{
	echo "Failed";
}

?>