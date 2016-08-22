<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Estadistica de ${page.getTitle()}</title>
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
						<b>Estadistica de ${page.getTitle()}</b>
					</div>

				</div>
				<div align="right">
					<a class="btn btn-primary" href="javascript:window.history.back();" role="button">Atras</a>
				</div>
			</div>
			<div class="panel-body">
				<ul class="list-group">
					<li class="list-group-item">Esta Pagina posee un total de:
						${totalRevisiones} revisiones</li>
					<li class="list-group-item"><div
							id="distribucionDeAporte_piechart"
							style="width: 900px; height: 500px;"></div></li>
					<li class="list-group-item">
						<div id="revisionesDia_dashboard_div">
							<!--Divs that will hold each control and chart-->
							<div id="revisionesDia_filter_div"></div>
							<div id="revisionesDia_chart_div"></div>
						</div>
					</li>
					<li class="list-group-item">
						<div id="contenidoDia_dashboard_div">
							<!--Divs that will hold each control and chart-->
							<div id="contenidoDia_filter_div"></div>
							<div id="contenidoDia_chart_div"></div>
						</div>
					</li>
					<li class="list-group-item">
						<div id="contenidoDia_dashboard_div">
							<div id="categoriasTiempo_chart_div">
									<c:if test="${empty categories}">
									Nunca fue categorizada.
									</c:if>
							</div>
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
			'packages' : [ 'corechart', 'controls', 'bar','timeline' ],
			'language' : 'es'
		});

		// Set a callback to run when the Google Visualization API is loaded.
		google.charts.setOnLoadCallback(distribucionDeAporte_drawChart);
		google.charts.setOnLoadCallback(revisionesDia_drawChart);
		google.charts.setOnLoadCallback(contenidoDia_drawChart);
		<c:if test="${not empty categories}">
		google.charts.setOnLoadCallback(categoriasTiempo_drawChart);
		</c:if>

		
		function distribucionDeAporte_drawChart() {

			var json = JSON.parse(' ${distribucionDeAporte} ');
			// Create the data table.
			var data = new google.visualization.DataTable();
			data.addColumn('string', 'Autores');
			data.addColumn('number', 'Revisiones');
			json.forEach(function(entry) {
				data.addRow([ entry[0], entry[1] ]); // Add a row with a string and a date value.
			});

			var options = {
				title : 'Revisiones de Autore'
			};

			var chart = new google.visualization.PieChart(document
					.getElementById('distribucionDeAporte_piechart'));

			chart.draw(data, options);
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
				'chartType' : 'ColumnChart',
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
		function contenidoDia_drawChart() {
			var json = JSON.parse(' ${contenidoDia} ');
			// Create the data table.
			var data = new google.visualization.DataTable();
			data.addColumn('date', 'Tiempo');
			data.addColumn('number', 'Bytes');
			json.forEach(function(entry) {
				data.addRow([ new Date(entry[0]), entry[1] ]); // Add a row with a string and a date value.
			});
			//data.addRows(json);
			//var data = google.visualization.arrayToDataTable(json);
			// Create a dashboard.
			var dashboard = new google.visualization.Dashboard(document
					.getElementById('contenidoDia_dashboard_div'));
			var linearRangeSlider = new google.visualization.ControlWrapper({
				'controlType' : 'ChartRangeFilter',
				'containerId' : 'contenidoDia_filter_div',
				'options' : {
					'filterColumnLabel' : 'Tiempo'
				}
			});

			var lineChart = new google.visualization.ChartWrapper({
				'chartType' : 'ColumnChart',
				'containerId' : 'contenidoDia_chart_div',
				'options' : {
					'title' : 'Contenido en el Tiempo',
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
		function categoriasTiempo_drawChart() {
		    var container = document.getElementById('categoriasTiempo_chart_div');
		    var chart = new google.visualization.Timeline(container);
		    var dataTable = new google.visualization.DataTable();

		    dataTable.addColumn({ type: 'string', id: 'Term' });
		    dataTable.addColumn({ type: 'string', id: 'Categoria' });
		    dataTable.addColumn({ type: 'date', id: 'Inicio' });
		    dataTable.addColumn({ type: 'date', id: 'Fin' });
		    var index=0;
			<c:forEach var="result" items="${categories}">   
				var index = index+1;
				var row = [index.toString()];
				row.push("<c:out value='${result.category.title}' />");
				row.push(new Date("<c:out value='${result.revisionStart.timestamp}'/>"));
				<c:if test="${not empty result.revisionEnd.timestamp}">
				row.push(new Date("<c:out value='${result.revisionEnd.timestamp}'/>"));
				</c:if>
				<c:if test="${empty result.revisionEnd.timestamp}">
				row.push(new Date());
				</c:if>
				dataTable.addRow(row);
	   		</c:forEach>


		    chart.draw(dataTable);
		  }
	</script>
</body>
</html>