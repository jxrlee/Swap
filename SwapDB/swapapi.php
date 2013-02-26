<?php

$servername = "localhost";
$username = "ai_purple";
$password = "XDmDgLBxAX3SFg";
$database = "ai_purple";

$mysqli;

if ($_GET['action'] == "getAllItems")
{

	$mysqli = mysqli_connect($servername, $username, $password, $database);
	$res = mysqli_query($mysqli, "SELECT * FROM items where available=1");
	$res->data_seek(0);
	$list = array();
	while ($row = mysqli_fetch_assoc($res))
	{
		$item = array();
		for ($i=0; $i<mysqli_num_fields($res); $i++)
	  	{
		 	$finfo = mysqli_fetch_field_direct($res,$i);
			$item[$finfo->name] = $row[$finfo->name];
		}

		$list[] = $item;
	}
	echo json_encode($list);

	$mysqli->close();
}

else if ($_GET['action'] == "getItemsById")
{

	$mysqli = mysqli_connect($servername, $username, $password, $database);
	$res = mysqli_query($mysqli, "SELECT * FROM items where available=1 and  sellerid=".$_GET['id']);
	$res->data_seek(0);
	$list = array();
	while ($row = mysqli_fetch_assoc($res))
	{
		$item = array();
		for ($i=0; $i<mysqli_num_fields($res); $i++)
	  	{
		 	$finfo = mysqli_fetch_field_direct($res,$i);
			$item[$finfo->name] = $row[$finfo->name];
		}

		$list[] = $item;
	}
	echo json_encode($list);

	$mysqli->close();
}

if ($_GET['action'] == "getItemsBySearch")
{
	$query = "SELECT * FROM items WHERE available = 1";
	$option = 0;
	$searchString = "";

	if (isset($_GET['option']))
	{
		$option = $_GET['option'];
	}
	if (isset($_GET['search']))
	{
		$searchString = $_GET['search'];
	}

	/*
	 * OPTIONS
	 *
	 * 0 = recent
	 * 1 = price
	 * 2 = premium
	 * 3 = nearby
	 *
	 */
	if ($option == 1)
	{
		$query .= " ORDER BY price";
	}
	else if ($option == 2) {
		$query .= " AND featured = 1";
	}

	$mysqli = mysqli_connect($servername, $username, $password, $database);
	$res = mysqli_query($mysqli, $query);
	$res->data_seek(0);
	$list = array();
	while ($row = mysqli_fetch_assoc($res))
	{
		$item = array();
		for ($i=0; $i<mysqli_num_fields($res); $i++)
	  	{
		 	$finfo = mysqli_fetch_field_direct($res,$i);
			$item[$finfo->name] = $row[$finfo->name];
		}

		$list[] = $item;
	}
	echo json_encode($list);

	$mysqli->close();
}

else if ($_GET['action'] == "getHistoryItemsByUserId")
{
	$mysqli = mysqli_connect($servername, $username, $password, $database);
	$res = mysqli_query($mysqli, "SELECT * FROM items where available=0 and sellerid=".$_GET['id']);
	$res->data_seek(0);
	$list = array();
	while ($row = mysqli_fetch_assoc($res))
	{
		$item = array();
		for ($i=0; $i<mysqli_num_fields($res); $i++)
	  	{
		 	$finfo = mysqli_fetch_field_direct($res,$i);
			$item[$finfo->name] = $row[$finfo->name];
		}

		$list[] = $item;
	}
	echo json_encode($list);

	$mysqli->close();
}

else if ($_GET['action'] == "getUserInfo")
{

	$mysqli = mysqli_connect($servername, $username, $password, $database);
	$res = mysqli_query($mysqli, "SELECT * FROM users where phone=".$_GET['id']);
	$res->data_seek(0);

	$row = mysqli_fetch_assoc($res);

		$item = array();
		for ($i=0; $i<mysqli_num_fields($res); $i++)
	  	{
		 	$finfo = mysqli_fetch_field_direct($res,$i);
			$item[$finfo->name] = $row[$finfo->name];
		}

	echo json_encode($item);

	$mysqli->close();
}

else if ($_GET['action'] == "insertNewItem")
{
	$mysqli = new mysqli($servername, $username, $password, $database);

	try{
		$stmt1 = $mysqli->prepare("SELECT count(*) FROM users where phone =?" );
		$stmt1->bind_param("s",$_GET['sellerid']);
		$stmt1->execute();
		$stmt1->bind_result($count);
		$stmt1->close();

	    if($count == 0)
	    {
			$stmt2 = $mysqli->prepare("INSERT INTO users(phone) VALUES (?)");
			$stmt2->bind_param("s",$_GET['sellerid']);
			$stmt2->execute();
			$stmt2->close();
		}

	}
	catch(Exception $e)
	{
		/*echo $e->getMessage();*/
	}

	if (!($stmt = $mysqli->prepare("INSERT INTO items(title,price,description,sellerid,location,date, featured, rating, available, imagesnum) VALUES (?,?,?,?,?,?,?,?,?,?)")))
	{
		header('HTTP/1.0 400 BAD REQUEST', true, 400);
		echo "Prepare failed: (" . $mysqli->errno . ") " . $mysqli->error;
	}

	if (!$stmt->bind_param("sdssssiiii",
	$_GET['title'],$_GET['price'],$_GET['description'],$_GET['sellerid'],$_GET['location'],$_GET['date'],$_GET['featured'],$_GET['rating'],$_GET['available'],$_GET['imagesnum']))
	{
		header('HTTP/1.0 400 BAD REQUEST', true, 400);
		echo "Binding parameters failed: (" . $stmt->errno . ") " . $stmt->error;
	}

	if (!$stmt->execute())
	{
		header('HTTP/1.0 400 BAD REQUEST', true, 400);
		echo "Execute failed: (" . $stmt->errno . ") " . $stmt->error;
	}
	else
	{
		$newid = array("id"=>$stmt->insert_id);
		echo json_encode($newid);
	}

	$stmt->close();
	$mysqli->close();
}
else if ($_GET['action'] == "updateItemById")
{
	$mysqli = new mysqli($servername, $username, $password, $database);

	if (!($stmt = $mysqli->prepare("UPDATE items SET title=?,price=?,description=?,location=?,date=?, featured=?, rating=?, available=?, imagesnum=? WHERE id=?")))
		{
			header('HTTP/1.0 400 BAD REQUEST', true, 400);
			echo "Prepare failed: (" . $mysqli->errno . ") " . $mysqli->error;
		}

		if (!$stmt->bind_param("sdsssiiiii",
		$_GET['title'],$_GET['price'],$_GET['description'],$_GET['location'],$_GET['date'],$_GET['featured'],$_GET['rating'],$_GET['available'],$_GET['imagesnum'],$_GET['id']))
		{
			header('HTTP/1.0 400 BAD REQUEST', true, 400);
			echo "Binding parameters failed: (" . $stmt->errno . ") " . $stmt->error;
		}

		if (!$stmt->execute())
		{
			header('HTTP/1.0 400 BAD REQUEST', true, 400);
			echo "Execute failed: (" . $stmt->errno . ") " . $stmt->error;
		}
		else
		{
			$newid = array("id"=>$stmt->insert_id);
			echo json_encode($newid);
		}

		$stmt->close();
		$mysqli->close();

}

?>