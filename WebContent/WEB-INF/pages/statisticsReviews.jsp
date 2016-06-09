<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Estadistica de Revisiones</title>
<!-- Bootstrap CSS -->
<link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet">
</head>
<body class=".container-fluid">
	<div class="container myrow-container">
		<div class="panel panel-success">
			<div class="panel-heading">
				<h3 class="panel-title">
					<b>Estadistica de Revisiones</b>
				</h3>
			</div>
			<div class="panel-body">
				<c:if test="${empty diffResult}">
						No hay diff
				</c:if>
				<ul class="list-group">
					<li class="list-group-item">En la Wiki hay un total de:
						${totalRevisiones}</li>
					<li class="list-group-item">Un promedio de
						${promedioPorPagina} revisiones por pagina</li>
					<li class="list-group-item"><div id="paginasConXRevisiones"></div></li>
					<li class="list-group-item">Porta ac consectetur ac</li>
					<li class="list-group-item">Vestibulum at eros</li>
				</ul>
			</div>
		</div>
	</div>


	<script src="<c:url value="/resources/js/jquery-2.2.3.js"/>"></script>
	<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
	<script type="text/javascript"
		src="https://www.gstatic.com/charts/loader.js"></script>
	<script type="text/javascript">
		// Load the Visualization API and the corechart package.
		google.charts.load('current', {
			'packages' : [ 'bar' ]
		});

		// Set a callback to run when the Google Visualization API is loaded.
		google.charts.setOnLoadCallback(drawChart);

		// Callback that creates and populates a data table,
		// instantiates the pie chart, passes in the data and
		// draws it.
		function drawChart() {
			var json = JSON.parse(' ${paginasConXRevisiones} ');
			// Create the data table.
			var data = google.visualization.arrayToDataTable(json);

			// Set chart options
			var options = {
				width : 900,
				chart : {
					title : 'Distribucion de revisiones en paginas',
					subtitle : '',
				},
				series : {
					0 : {
						axis : 'Paginas'
					}
				
				// Bind series 0 to an axis named 'distance'.
				},
				axes : {
					y : {
						Paginas : {
							label : 'Paginas'
						}
					// Left y-axis.
					}
					
				}
			};

			// Instantiate and draw our chart, passing in some options.
			var chart = new google.charts.Bar(document
					.getElementById('paginasConXRevisiones'));
			chart.draw(data, options);
		}
	</script>
</body>
</html>