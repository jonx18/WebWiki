<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="diffstatisticsofpage.titlepage" /></title>
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
						<b><spring:message code="diffstatisticsofpage.title" /></b>
					</div>
				</div>
				<div align="right">
					<a class="btn btn-primary" href="javascript:window.history.back();" role="button">
					<spring:message code="diffstatisticsofpage.back" /></a>
				</div>
			</div>
			<div class="panel-body">
				<ul class="list-group">

					<li class="list-group-item">
						<div id="estilosEnElTiempo_dashboard_div">
							<!--Divs that will hold each control and chart-->
							<div id="estilosEnElTiempo_filter_div"></div>
							<div id="estilosEnElTiempo_filter_div2"></div>
							<div id="estilosEnElTiempo_chart_div"></div>
							<c:if test="${empty mapStyleChanges}">
								<spring:message code="diffstatisticsofpage.estilosEnElTiempo.error" />
							</c:if>
						</div>
					</li>
					<li class="list-group-item">
						<div id="contenidoDia_dashboard_div">
							<!--Divs that will hold each control and chart-->
							<div id="contenidoDia_filter_div"></div>
							<div id="contenidoDia_chart_div"></div>
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
			'packages' : [ 'corechart', 'controls', 'bar' ],
			'language' : '<spring:message code="index.languageSelector.active" />'
		});

		// Set a callback to run when the Google Visualization API is loaded.
		//google.charts.setOnLoadCallback(paginasEnNamespace_drawChart);
		//google.charts.setOnLoadCallback(nuevasPaginasDia_drawChart);
		<c:if test="${not empty mapStyleChanges}">
				google.charts.setOnLoadCallback(estilosEnElTiempo_drawChart);
		</c:if>
		google.charts.setOnLoadCallback(contenidoDia_drawChart);
		//google.charts.setOnLoadCallback(paginasConXRevisiones_drawChart);
	

		function estilosEnElTiempo_drawChart() {
			//var json = JSON.parse(' ${nuevasPaginasPorNamespaceDia} ');
			// Create the data table.
			var data = new google.visualization.DataTable();
			data.addColumn('date', '<spring:message code="diffstatisticsofpage.estilosEnElTiempo.colum1" />');
			<c:forEach var="result" items="${mapStyleChanges}">   
				data.addColumn('number', '${result.key.getName()}');
		    </c:forEach>
		    <c:forEach begin="1" end="${fn:length(dates)}" var="val">
		    	var row = [new Date("<c:out value='${dates[val-1]}'/>")];
				<c:forEach var="result" items="${mapStyleChanges}">   
					row.push(<c:out value="${result.value[val-1]}" />);
		    	</c:forEach>
		    	data.addRow(row);
			</c:forEach>

			// Create a dashboard.
			var dashboard = new google.visualization.Dashboard(
					document
							.getElementById('estilosEnElTiempo_dashboard_div'));
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
			initState.selectedValues.push(data.getColumnLabel(0));
			var columnFilter = new google.visualization.ControlWrapper({
				'controlType' : 'CategoryFilter',
				'containerId' : 'estilosEnElTiempo_filter_div',
				'dataTable' : columnsTable,
				'options' : {
					filterColumnLabel : 'colLabel',
					ui : {
						label : '<spring:message code="diffstatisticsofpage.estilosEnElTiempo.label1" />',
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
				'containerId' : 'estilosEnElTiempo_filter_div2',
				'dataTable' : data,
				'options' : {
					'filterColumnLabel' : '<spring:message code="diffstatisticsofpage.estilosEnElTiempo.filterColumnLabel" />'
				}
			});

			var lineChart = new google.visualization.ChartWrapper({
				'chartType' : 'LineChart',
				'containerId' : 'estilosEnElTiempo_chart_div',
				dataTable : data,
				'options' : {
					'title' : '<spring:message code="diffstatisticsofpage.estilosEnElTiempo.title" />',
					'subtitle' : '',
					'bars' : 'vertical',
					'vAxis' : {
						'format' : 'long',
						'maxValue' : {
							'count' : 1000
						}
					},

					'height' : 400,
					'pieSliceText' : '<spring:message code="diffstatisticsofpage.estilosEnElTiempo.pieSliceText" />',
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
		function contenidoDia_drawChart() {
			// Create the data table.
			var data = new google.visualization.DataTable();
			data.addColumn('date', '<spring:message code="diffstatisticsofpage.contenidoDia.colum1" />');
			data.addColumn('number', '<spring:message code="diffstatisticsofpage.contenidoDia.colum2" />');
		    <c:forEach begin="1" end="${fn:length(dates)}" var="val">
	    		var row = [new Date("<c:out value='${dates[val-1]}'/>")];
	    		var total = 0;
				<c:forEach var="result" items="${mapStyleChanges}">   
					total = total + <c:out value="${result.value[val-1]}" />;
					
	    		</c:forEach>
	    		row.push(total);
	    		data.addRow(row);
			</c:forEach>

			//data.addRows(json);
			//var data = google.visualization.arrayToDataTable(json);
			// Create a dashboard.
			var dashboard = new google.visualization.Dashboard(document
					.getElementById('contenidoDia_dashboard_div'));
			var linearRangeSlider = new google.visualization.ControlWrapper({
				'controlType' : 'ChartRangeFilter',
				'containerId' : 'contenidoDia_filter_div',
				'options' : {
					'filterColumnLabel' : '<spring:message code="diffstatisticsofpage.contenidoDia.filterColumnLabel" />'
				}
			});

			var lineChart = new google.visualization.ChartWrapper({
				'chartType' : 'ColumnChart',
				'containerId' : 'contenidoDia_chart_div',
				'options' : {
					'title' : '<spring:message code="diffstatisticsofpage.contenidoDia.title" />',
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
					'pieSliceText' : '<spring:message code="diffstatisticsofpage.contenidoDia.pieSliceText" />',
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