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
				<div class="panel-title">
					<div align="left">
						<b>Estadistica de Revisiones</b>
					</div>

				</div>
				<div align="right">
					<a class="btn btn-primary" href="/WikiWebTest/" role="button">Atras</a>
				</div>
			</div>
			<div class="panel-body">
				<ul class="list-group">
					<li class="list-group-item">En la Wiki hay un total de:
						${totalRevisiones} revisiones</li>
					<li class="list-group-item">Un promedio de
						${promedioPorPagina} revisiones por pagina</li>
					<li class="list-group-item"><div
							id="revisionesEnNamespace_piechart"
							style="width: 900px; height: 500px;"></div></li>
					<li class="list-group-item">
						<div id="paginasConXRevisiones_dashboard_div">
							<!--Divs that will hold each control and chart-->
							<div id="paginasConXRevisiones_filter_div"></div>
							<div id="paginasConXRevisiones_chart_div"></div>
						</div>
					</li>
					<li class="list-group-item">
						<div id="revisionesDia_dashboard_div">
							<!--Divs that will hold each control and chart-->
							<div id="revisionesDia_filter_div"></div>
							<div id="revisionesDia_chart_div"></div>
						</div>
					</li>
				</ul>
			</div>
		</div>

	</div>


	<script src="<c:url value="/resources/js/jquery-2.2.4.js"/>"></script>
	<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
	<script type="text/javascript"
		src="https://www.gstatic.com/charts/loader.js"></script>
	<script type="text/javascript">
		// Load the Visualization API and the corechart package.
		google.charts.load('current', {
			'packages' : [ 'corechart', 'controls' ],
			'language' : 'es'
		});

		// Set a callback to run when the Google Visualization API is loaded.
		google.charts.setOnLoadCallback(revisionesEnNamespace_drawChart);
		google.charts.setOnLoadCallback(paginasConXRevisiones_drawChart);
		google.charts.setOnLoadCallback(revisionesDia_drawChart);

		function revisionesEnNamespace_drawChart() {

			var json = JSON.parse(' ${revisionesEnNamespace} ');
			// Create the data table.
			var data = new google.visualization.DataTable();
			data.addColumn('string', 'Namespace');
			data.addColumn('number', 'Revisiones');
			json.forEach(function(entry) {
				data.addRow([ entry[0], entry[1] ]); // Add a row with a string and a date value.
			});

			var options = {
				title : 'Revisiones en Namespaces'
			};

			var chart = new google.visualization.PieChart(document
					.getElementById('revisionesEnNamespace_piechart'));

			chart.draw(data, options);
		}
		// Callback that creates and populates a data table,
		// instantiates the pie chart, passes in the data and
		// draws it.
		function paginasConXRevisiones_drawChart() {
			var json = JSON.parse(' ${paginasConXRevisiones} ');
			// Create the data table.
			var data = google.visualization.arrayToDataTable(json);
			// Create a dashboard.
			var dashboard = new google.visualization.Dashboard(document
					.getElementById('paginasConXRevisiones_dashboard_div'));
			var linearRangeSlider = new google.visualization.ControlWrapper({
				'controlType' : 'NumberRangeFilter',
				'containerId' : 'paginasConXRevisiones_filter_div',
				'options' : {
					'filterColumnLabel' : 'Revisiones'
				}
			});
			/* 			// Set chart options
			 var options = {
			 chart : {
			 title : 'Distribucion de revisiones en paginas',
			 subtitle : '',
			 },

			 bars : 'vertical',
			 vAxis : {
			 format : 'long',
			 maxValue : {
			 count : 1000
			 }
			 },
			 height : 400,
			 colors : [ '#1b9e77' ]
			 }; */
			var lineChart = new google.visualization.ChartWrapper({
				'chartType' : 'LineChart',
				'containerId' : 'paginasConXRevisiones_chart_div',
				'options' : {
					'title' : 'Distribucion de revisiones en paginas',
					'subtitle' : '',
					'bars' : 'vertical',
					'vAxis' : {
						'format' : 'long',
						'maxValue' : {
							'count' : 1000
						}
					},
					'colors' : [ '#1b9e77' ],
					'height' : 400,
					'pieSliceText' : 'Revisiones',
					'legend' : 'right'
				}
			});
			// Establish dependencies, declaring that 'filter' drives 'pieChart',
			// so that the pie chart will only display entries that are let through
			// given the chosen slider range.
			dashboard.bind(linearRangeSlider, lineChart);

			// Draw the dashboard.
			dashboard.draw(data);
			// Instantiate and draw our chart, passing in some options.
			/* var chart = new google.visualization.LineChart(document
					.getElementById('paginasConXRevisiones'));
			chart.draw(data, options); */
		}
		function revisionesDia_drawChart() {
			var json = JSON.parse(' ${revisionesDia} ');
			// Create the data table.
			var data = new google.visualization.DataTable();
			data.addColumn('date', 'Tiempo');
			data.addColumn('number', 'Revisiones');
			json.forEach(function(entry) {
				data.addRow([ new Date(entry[0]), entry[1] ]); // Add a row with a string and a date value.
			});
			//data.addRows(json);
			//var data = google.visualization.arrayToDataTable(json);
			// Create a dashboard.
			var dashboard = new google.visualization.Dashboard(document
					.getElementById('revisionesDia_dashboard_div'));
			var linearRangeSlider = new google.visualization.ControlWrapper({
				'controlType' : 'ChartRangeFilter',
				'containerId' : 'revisionesDia_filter_div',
				'options' : {
					'filterColumnLabel' : 'Tiempo'
				}
			});

			var lineChart = new google.visualization.ChartWrapper({
				'chartType' : 'LineChart',
				'containerId' : 'revisionesDia_chart_div',
				'options' : {
					'title' : 'Revisiones en el Tiempo',
					'subtitle' : '',
					'bars' : 'vertical',
					'vAxis' : {
						'format' : 'long',
						'maxValue' : {
							'count' : 1000
						}
					},
					'colors' : [ '#1b9e77' ],
					'height' : 400,
					'pieSliceText' : 'Tiempo',
					'legend' : 'right'
				}
			});
			// Establish dependencies, declaring that 'filter' drives 'pieChart',
			// so that the pie chart will only display entries that are let through
			// given the chosen slider range.
			dashboard.bind(linearRangeSlider, lineChart);

			// Draw the dashboard.
			dashboard.draw(data);
			// Instantiate and draw our chart, passing in some options.
			/* var chart = new google.visualization.LineChart(document
					.getElementById('paginasConXRevisiones'));
			chart.draw(data, options); */
		}
	</script>
</body>
</html>