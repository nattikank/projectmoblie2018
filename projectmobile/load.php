<?php

$con = mysqli_connect("localhost","root","","photo");

$sql = 'SELECT * FROM user where id="'.$_POST['id'].'"';
$result = mysqli_query($con,$sql);

$num = mysqli_num_rows($result);
if($num > 0){
	while ($frin = mysqli_fetch_array($result)) {
		$nongfah = array();
	    array_push($nongfah,array("id"=>$frin["id"],
		"username"=>$frin["username"],"Name"=>$frin["Name"],
		"tel"=>$frin["tel"],"idcard"=>$frin["idcard"],
		"address"=>$frin["address"],"type"=>$frin["type_member"],
		"image"=>$frin["img"],"pass"=>$frin['password'],"qr"=>$frin['qr']));
	}
	echo json_encode($nongfah);
}else{
	echo "Failed";
}

?>