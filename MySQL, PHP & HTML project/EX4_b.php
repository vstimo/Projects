<html>
	<head>
	<title>Exercitiul 4</title>
		<style>
		.topleft {
		position: absolute;
		top: 22%;
		left: 40%;
		font-size: 30px;
		}
		
		.bottomright {
		position: absolute;
		bottom: 8px;
		right: 16px;
		font-size: 18px;
		}
	</style>
	</head>
	
	<body style = "background-color:dcdcdc;">
	
	<table border = "3" width = "100%" height = "100%" cellpadding = "0" cellspacing = "0">
	
	<tr>
	<!-- Row1, Cell 1: Border -->
	<td style = "background-image: url('fundal.jpg')" colspan ="2" height = "20%">
	</td>
	</tr>
	
	<tr>
	<!-- Row2, Cell 1: Navigation -->
	<td class = "nav" style = "background-color:ce92ef" width = "15%" height = "80%">
	<a href = "http://localhost/proiect/Ex3.php"><p style="text-align:center; font-family:monospace; color:black; font-size:20px ">Exercițiul 3</p></a>
	<a href = "http://localhost/proiect/Ex4.php"><p style="text-align:center; font-family:monospace; color:black; font-size:20px ">Exercițiul 4</p></a>
	<a href = "http://localhost/proiect/Ex5.php"><p style="text-align:center; font-family:monospace; color:black; font-size:20px ">Exercițiul 5</p></a>
	<a href = "http://localhost/proiect/Ex6.php"><p style="text-align:center; font-family:monospace; color:black; font-size:20px ">Exercițiul 6</p></a>
	</td>
	
	<!-- Row2, Cell 2: Body -->
	<td style = "background-color:white" width = "80%" height = "80%">
	<p class="topleft" style = "font-family:monospace; text-align:center;"><b>Colocviu final de laborator BD</b></p>
	<p class="bottomright" style = "font-family:monospace; text-align:center;">Morar Timotei Cristian, grupa 30225</p>
	<table width="300px" border="1px">
	<tr>
	<td width = "20px" style="background-color:aa58d6; text-align:center;">Nr. crt</td>
	<td width = "135px"style="background-color:aa58d6; text-align:center;">Rețetă_id1</td>
	<td style="background-color:aa58d6; text-align:center;">Rețetă_id2</td>
	</tr>
	
	<tr>
	<td style="text-align:center;">
	<?php
	function interogare4_b1(){
		
		require 'conectare.php';

		$sql = "SELECT DISTINCT least(a.reteta_id,b.reteta_id),greatest(a.reteta_id,b.reteta_id)
				FROM (set_ingrediente a JOIN set_ingrediente b ON a.ingred_id=b.ingred_id)
				WHERE (a.ingred_id=b.ingred_id and a.cantitate=b.cantitate and a.um=b.um and a.reteta_id!=b.reteta_id);";
		$result = mysqli_query($conectare, $sql);
		$aux = 0;
		while($row = $result->fetch_assoc()){
			$aux++;
			echo $aux.'<br>';
		}
		}
		
		interogare4_b1();
	?>
	</td>
	
	<td style="text-align:center;">
	<?php
	function interogare4_b2(){
		
		require 'conectare.php';
		
		$sql = "SELECT DISTINCT least(a.reteta_id,b.reteta_id),greatest(a.reteta_id,b.reteta_id)
				FROM (set_ingrediente a JOIN set_ingrediente b ON a.ingred_id=b.ingred_id)
				WHERE (a.ingred_id=b.ingred_id and a.cantitate=b.cantitate and a.um=b.um and a.reteta_id!=b.reteta_id);";
			
			$result = mysqli_query($conectare, $sql);
			while($row = $result->fetch_assoc()){
				echo $row['least(a.reteta_id,b.reteta_id)'].'<br>';
			}
		}
		
		interogare4_b2();
	?>
	</td>
	
	<td style="text-align:center;">
	<?php
	function interogare4_b3(){
		
		require 'conectare.php';
		
		$sql = "SELECT DISTINCT least(a.reteta_id,b.reteta_id),greatest(a.reteta_id,b.reteta_id)
				FROM (set_ingrediente a JOIN set_ingrediente b ON a.ingred_id=b.ingred_id)
				WHERE (a.ingred_id=b.ingred_id and a.cantitate=b.cantitate and a.um=b.um and a.reteta_id!=b.reteta_id);";
			
			$result = mysqli_query($conectare, $sql);
			while($row = $result->fetch_assoc()){
				echo $row['greatest(a.reteta_id,b.reteta_id)'].'<br>';
			}
		}
		
		interogare4_b3();
	?>
	</td>
	</tr>
	</table>
	<br>
	<table border = 2 width="10%" cellpadding = "0" cellspacing = "0">
		<tr>
			<th><a href = "http://localhost/proiect/Ex4.php"><p style="text-align:center;">Inapoi</p></a></th>
		</tr>
	</table>
	</td>
	</tr>
	</table>
	</body>
</html>