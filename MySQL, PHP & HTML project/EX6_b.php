<html>
	<head>
	<title>Exercitiul 5</title>
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
	<form action="" method="post" required>
            Introduceți ingredientul căutat in rețete tip Tocană:
            <input type=text name="t1">
            <br>
            <br>
            <input type=submit name="s">
	<br>
	</form>
	<table width="300px" border="1px">
	<tr>
	<td width = "20px" style="background-color:aa58d6; text-align:center;">Nr. crt</td>
	<td style="background-color:aa58d6; text-align:center;">Cantitate</td>
	</tr>
	
	<tr>
	<td style = "text-align:center;"> 
	<?php
	function interogare6_b1(){
		
		require 'conectare.php';
		
		$valoare = 0;
		$sql = 0;
		if(isset($_POST['s'])){
			$valoare = $_POST['t1'];
		}
			if($valoare!="" && $valoare!=0){
			$sql = "SELECT SUM(
					CASE set_ingrediente.um 
					WHEN 'gr' THEN set_ingrediente.cantitate 
					WHEN 'buc' THEN set_ingrediente.cantitate*4
					ELSE set_ingrediente.cantitate*36 end)
					FROM set_ingrediente
					  INNER JOIN reteta ON set_ingrediente.reteta_id=reteta.reteta_id
					  INNER JOIN categorie ON reteta.categ_id=categorie.categ_id
					WHERE LOWER(categorie.tip)='tocana' AND set_ingrediente.ingred_id=(SELECT ingred_id from ingredient WHERE LOWER(ingredient.ingredient)='$valoare');";
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
		
		interogare6_b1();
	?>
	</td>
	
	<td style = "text-align:center;">
	<?php
	function interogare6_b2(){
		
		require 'conectare.php';
		
		$valoare = 0;
		$sql = 0;
		if(isset($_POST['s'])){
			$valoare = $_POST['t1'];
		}
			if($valoare!="" && $valoare!=0){
			$valoare=strtolower($valoare);
			$sql = "SELECT SUM(CASE 
			WHEN set_ingrediente.um='gr' THEN set_ingrediente.cantitate  
			WHEN set_ingrediente.um='buc' THEN set_ingrediente.cantitate*4 
			ELSE set_ingrediente.cantitate*36 end) 
			FROM set_ingrediente INNER JOIN reteta ON set_ingrediente.reteta_id=reteta.reteta_id INNER JOIN categorie ON reteta.categ_id=categorie.categ_id 
			WHERE LOWER(categorie.tip)='tocana' AND set_ingrediente.ingred_id=(SELECT ingred_id from ingredient WHERE LOWER(ingredient.ingredient)='$valoare');";
			}
			
			if($sql!=0){
			$result = mysqli_query($conectare, $sql);
			while($row = $result->fetch_assoc()){
				echo $row['SUM(CASE 
			WHEN set_ingrediente.um=\'gr\' THEN set_ingrediente.cantitate  
			WHEN set_ingrediente.um=\'buc\' THEN set_ingrediente.cantitate*4 
			ELSE set_ingrediente.cantitate*36 end)'].'<br>';
			}
			}
		}
		interogare6_b2();
	?>
	
	</tr>
	</table>
	<br>
	<table border = 2 width="10%" cellpadding = "0" cellspacing = "0">
		<tr>
			<th><a href = "http://localhost/proiect/Ex6.php"><p style="text-align:center;">Inapoi</p></a></th>
		</tr>
	</table>
	</td>
	</tr>
	</table>
	</body>
</html>