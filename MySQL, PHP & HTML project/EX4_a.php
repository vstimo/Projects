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
	<form action="" method="post">
            Introduceți ingredientul pe care <b>nu</b> vreți să îl conțină rețetele:
            <input type=text name="t1">
            <br>
            <br>
            <input type=submit name="s">
	<br>
	</form>
	<table width="800px" border="1px">
	<tr>
	<td width = "20px" style="background-color:aa58d6; text-align:center;">Nr. crt</td>
	<td width = "200px"style="background-color:aa58d6; text-align:center;">Nume</td>
	<td style="background-color:aa58d6; text-align:center;">Descriere</td>
	<td width = "100px" style="background-color:aa58d6; text-align:center;">Categ_id</td>
	</tr>
	
	<tr>
	<td style="text-align:center;">
	<?php
	function interogare4_a4(){
		
		require 'conectare.php';
		
		$valoare = 0;
		$sql = 0;
		if(isset($_POST['s'])){
			$valoare = $_POST['t1'];
		}
			if($valoare!="" && $valoare!=0){
			$valoare=strtolower($valoare);
			$sql = "SELECT nume
					FROM reteta
					WHERE reteta_id NOT IN (
					SELECT reteta_id
					FROM reteta JOIN set_ingrediente USING (reteta_id) JOIN ingredient USING (ingred_id)
					WHERE LOWER(ingredient.ingredient)='$valoare');";
			}
			
			if($sql!=0){
			$result = mysqli_query($conectare, $sql);
			$aux = 0;
			while($row = $result->fetch_assoc()){
				$aux++;
				echo $aux.'<br>';
			}
			}
		}
		
		interogare4_a4();
	?>
	</td>
	
	<td style="text-align:center;">
	<?php
	function interogare4_a(){
		
		require 'conectare.php';
		
		$valoare = 0;
		$sql = 0;
		if(isset($_POST['s'])){
			$valoare = $_POST['t1'];
		}
			if($valoare!="" && $valoare!=0){
			$valoare=strtolower($valoare);
			$sql = "SELECT nume
					FROM reteta
					WHERE reteta_id NOT IN (
					SELECT reteta_id
					FROM reteta JOIN set_ingrediente USING (reteta_id) JOIN ingredient USING (ingred_id)
					WHERE LOWER(ingredient.ingredient)='$valoare');";
			}
			
			if($sql!=0){
			$result = mysqli_query($conectare, $sql);
			while($row = $result->fetch_assoc()){
				echo $row['nume'].'<br>';
			}
			}
		}
		
		interogare4_a();
	?>
	</td>
	
	<td>
	<?php
	function interogare4_a2(){
		
		require 'conectare.php';
		
		$valoare = 0;
		$sql = 0;
		if(isset($_POST['s'])){
			$valoare = $_POST['t1'];
		}
			if($valoare!="" && $valoare!=0){
			$valoare=strtolower($valoare);
			$sql = "SELECT descriere
					FROM reteta
					WHERE reteta_id NOT IN (
					SELECT reteta_id
					FROM reteta JOIN set_ingrediente USING (reteta_id) JOIN ingredient USING (ingred_id)
					WHERE LOWER(ingredient.ingredient)='$valoare');";
			}
			
			if($sql!=0){
			$result = mysqli_query($conectare, $sql);
			while($row = $result->fetch_assoc()){
				echo $row['descriere'].'<br>';
			}
			}
		}
		
		interogare4_a2();
	?>
	</td>
	
	<td style="text-align:center;">
	<?php
	function interogare4_a3(){
		
		require 'conectare.php';
		
		$valoare = 0;
		$sql = 0;
		if(isset($_POST['s'])){
			$valoare = $_POST['t1'];
		}
			if($valoare!="" && $valoare!=0){
			$valoare=strtolower($valoare);
			$sql = "SELECT categ_id
					FROM reteta
					WHERE reteta_id NOT IN (
					SELECT reteta_id
					FROM reteta JOIN set_ingrediente USING (reteta_id) JOIN ingredient USING (ingred_id)
					WHERE LOWER(ingredient.ingredient)='$valoare');";
			}
			
			if($sql!=0){
			$result = mysqli_query($conectare, $sql);
			while($row = $result->fetch_assoc()){
				echo $row['categ_id'].'<br>';
			}
			}
		}
		
		interogare4_a3();
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