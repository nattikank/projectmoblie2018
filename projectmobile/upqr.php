<?php

$con = mysqli_connect("localhost","root","","photo");

$sql = 'UPDATE user set qr="'.$_POST['qr'].'"where id="'.$_POST['id'].'"';

$req = mysqli_query($con,$sql);

if($req){
	echo "Success";
}else{
	echo "Failed";
}