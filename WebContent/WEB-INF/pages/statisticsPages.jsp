<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Estadistica de Paginas</title>
<!-- Bootstrap CSS -->
<link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet">
</head>
<body class=".container-fluid">
	<div class="container myrow-container">
		<div class="panel panel-success">
			<div class="panel-heading">
				<h3 class="panel-title">
					<b>Estadistica de Paginas</b>
				</h3>
			</div>
			<div class="panel-body">
				<ul class="list-group">
					<li class="list-group-item">En la Wiki hay un total de:
						${totalPaginas} paginas</li>
					<li class="list-group-item"><div
							id="paginasEnNamespace_piechart"
							style="width: 900px; height: 500px;"></div></li>
					<li class="list-group-item">
						<div id="nuevasPaginasDia_dashboard_div">
							<!--Divs that will hold each control and chart-->
							<div id="nuevasPaginasDia_filter_div"></div>
							<div id="nuevasPaginasDia_chart_div"></div>
						</div>
					</li>
					<li class="list-group-item">
						<div id="nuevasPaginasPorNamespaceDia_dashboard_div">
							<!--Divs that will hold each control and chart-->
							<div id="nuevasPaginasPorNamespaceDia_filter_div"></div>
							<div id="nuevasPaginasPorNamespaceDia_filter_div2"></div>
							<div id="nuevasPaginasPorNamespaceDia_chart_div"></div>
						</div>
					</li>
					<li class="list-group-item">
						<div id="paginasConXRevisiones_dashboard_div">
							<!--Divs that will hold each control and chart-->
							<div id="paginasConXRevisiones_filter_div"></div>
							<div id="paginasConXRevisiones_filter_div2"></div>
							<div id="paginasConXRevisiones_chart_div"></div>
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
		google.charts.setOnLoadCallback(paginasEnNamespace_drawChart);
		google.charts.setOnLoadCallback(nuevasPaginasDia_drawChart);
		google.charts.setOnLoadCallback(nuevasPaginasPorNamespaceDia_drawChart);

		function paginasEnNamespace_drawChart() {

			var json = JSON.parse(' ${paginasEnNamespace} ');
			// Create the data table.
			var data = new google.visualization.DataTable();
			data.addColumn('string', 'Namespace');
			data.addColumn('number', 'Paginas');
			json.forEach(function(entry) {
				data.addRow([ entry[0], entry[1] ]); // Add a row with a string and a date value.
			});

			var options = {
				title : 'Paginas en Namespaces'
			};

			var chart = new google.visualization.PieChart(document
					.getElementById('paginasEnNamespace_piechart'));

			chart.draw(data, options);
		}

		function nuevasPaginasDia_drawChart() {
			var json = JSON.parse(' ${nuevasPaginasDia} ');
			// Create the data table.
			var data = new google.visualization.DataTable();
			data.addColumn('date', 'Tiempo');
			data.addColumn('number', 'Nuevas Paginas');
			json.forEach(function(entry) {
				data.addRow([ new Date(entry[0]), entry[1] ]); // Add a row with a string and a date value.
			});
			//data.addRows(json);
			//var data = google.visualization.arrayToDataTable(json);
			// Create a dashboard.
			var dashboard = new google.visualization.Dashboard(document
					.getElementById('nuevasPaginasDia_dashboard_div'));

			var linearRangeSlider = new google.visualization.ControlWrapper({
				'controlType' : 'ChartRangeFilter',
				'containerId' : 'nuevasPaginasDia_filter_div',
				'options' : {
					'filterColumnLabel' : 'Tiempo'
				}
			});

			var lineChart = new google.visualization.ChartWrapper({
				'chartType' : 'LineChart',
				'containerId' : 'nuevasPaginasDia_chart_div',
				'options' : {
					'title' : 'Nuevas Paginas en el Tiempo',
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

		function nuevasPaginasPorNamespaceDia_drawChart() {
			var json = JSON.parse(' ${nuevasPaginasPorNamespaceDia} ');
			// Create the data table.
			var data = new google.visualization.DataTable();
			var head=true;
			json.forEach(function(entry) {
				if(head){					
					entry.forEach(function(entry2) {
						if(head){
							head=false;
							data.addColumn('date', 'Tiempo')
						}else{
							data.addColumn('number', entry2);
						}
						
						
					});
				}
				else{
				entry[0]=new Date(entry[0]);
				data.addRow(entry); // Add a row with a string and a date value.
				};
			}); 
			//data.addRows(json);
			//var data = google.visualization.arrayToDataTable(json);
			// Create a dashboard.
			var dashboard = new google.visualization.Dashboard(
					document.getElementById('nuevasPaginasPorNamespaceDia_dashboard_div'));
			var columnsTable = new google.visualization.DataTable();
			columnsTable.addColumn('number', 'colIndex');
			columnsTable.addColumn('string', 'colLabel');
			var initState = {
				selectedValues : []
			};
			// put the columns into this data table (skip column 0)
			for (var i = 1; i < data.getNumberOfColumns(); i++) {
				columnsTable.addRow([ i, data.getColumnLabel(i) ]);
				// you can comment out this next line if you want to have a default selection other than the whole list
				//initState.selectedValues.push(data.getColumnLabel(i));
			}
			initState.selectedValues.push(data.getColumnLabel(1));
			var columnFilter = new google.visualization.ControlWrapper({
				'controlType' : 'CategoryFilter',
				'containerId' : 'nuevasPaginasPorNamespaceDia_filter_div',
				'dataTable' : columnsTable,
				'options' : {
					filterColumnLabel : 'colLabel',
					ui : {
						label : 'Columns',
						allowTyping : false,
						allowMultiple : true,
						allowNone : false,
						selectedValuesLayout : 'belowStacked'
					}
				},
				'state' : initState
			});
			var linearRangeSlider = new google.visualization.ControlWrapper({
				'controlType' : 'ChartRangeFilter',
				'containerId' : 'nuevasPaginasPorNamespaceDia_filter_div2',
				'dataTable' : data,
				'options' : {
					'filterColumnLabel' : 'Tiempo'
				}
			});

			var lineChart = new google.visualization.ChartWrapper({
				'chartType' : 'LineChart',
				'containerId' : 'nuevasPaginasPorNamespaceDia_chart_div',
				dataTable: data,
				'options' : {
					'title' : 'Nuevas Paginas en el Tiempo por Namespace',
					'subtitle' : '',
					'bars' : 'vertical',
					'vAxis' : {
						'format' : 'long',
						'maxValue' : {
							'count' : 1000
						}
					},

					'height' : 400,
					'pieSliceText' : 'Tiempo',
					'legend' : 'right'
				}
			});
			// Establish dependencies, declaring that 'filter' drives 'pieChart',
			// so that the pie chart will only display entries that are let through
			// given the chosen slider range.

			function setChartView() {
				var state = columnFilter.getState();
				var row;
				var view = {
					columns : [ 0 ]
				};
				for (var i = 0; i < state.selectedValues.length; i++) {
					row = columnsTable.getFilteredRows([ {
						column : 1,
						value : state.selectedValues[i]
					} ])[0];
					view.columns.push(columnsTable.getValue(row, 0));
				}
				// sort the indices into their original order
				view.columns.sort(function(a, b) {
					return (a - b);
				});
				lineChart.setView(view);
				lineChart.draw();
				//linearRangeSlider.draw();
			}
			google.visualization.events.addListener(columnFilter,
					'statechange', setChartView);
			//dashboard.bind([linearRangeSlider,columnFilter], lineChart);

			
			//linearRangeSlider.draw();
			

			// Draw the dashboard.
			//dashboard.draw();
			columnFilter.draw();
			setChartView();
			// Instantiate and draw our chart, passing in some options.
			/* var chart = new google.visualization.LineChart(document
					.getElementById('paginasConXRevisiones'));
			chart.draw(data, options); */
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
	</script>
</body>
</html>