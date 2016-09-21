<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="difflist.titlepage" /></title>
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
						<b><spring:message code="difflist.title" /></b>
					</div>

				</div>
				<div align="right">
					<a class="btn btn-primary" href="javascript:window.history.back();" role="button">
<spring:message code="difflist.back" /></a>
				</div>
			</div>
			<div class="panel-body">
				<c:if test="${empty listListDiff}">
						<spring:message code="difflist.table.empty" />
				</c:if>
				<ul class="list-group">
					<c:if test="${not empty listListDiff}">
						<c:forEach items="${listListDiff}" var="listDiff">
							<div class="row">

								<div class="col-md-6">
									<c:if test="${listDiff.ischange}">
										<li class="list-group-item list-group-item-danger">
									</c:if>
									<c:if test="${not listDiff.ischange}">
										<li class="list-group-item list-group-item-info">
									</c:if>
									<c:forEach items="${listDiff.getDiffs()}" var="diff">
										<c:if test="${diff[0].operation == 'EQUAL'}">
											<!--  <li class="list-group-item list-group-item-info"><c:out value="${diff[0].text}" /></li>-->
											<c:out value="${diff[0].text}" />
										</c:if>

										<c:if test="${diff[0].operation == 'DELETE'}">
											<!-- <li class="list-group-item list-group-item-danger"><c:out value="${diff[0].text}" /></li>-->
											<del>
												<c:out value="${diff[0].text}" />
											</del>
										</c:if>
									</c:forEach>
									</li>
								</div>
								<div class="col-md-6">
									<c:if test="${listDiff.ischange}">
										<li class="list-group-item list-group-item-success">
									</c:if>
									<c:if test="${not listDiff.ischange}">
										<li class="list-group-item list-group-item-info">
									</c:if>
									<c:forEach items="${listDiff.getDiffs()}" var="diff">
										<c:if test="${diff[1].operation == 'EQUAL'}">
											<!-- <li class="list-group-item list-group-item-info"><c:out value="${diff[1].text}" /></li>-->
											<c:out value="${diff[1].text}" />
										</c:if>
										<c:if test="${diff[1].operation == 'INSERT'}">
											<!-- <li class="list-group-item list-group-item-success"><c:out value="${diff[1].text}" /></li>-->
											<ins>
												<c:out value="${diff[1].text}" />
											</ins>
										</c:if>
									</c:forEach>
									</li>
								</div>

							</div>
						</c:forEach>

					</c:if>
				</ul>
			</div>
		</div>
		<div class="panel panel-success">
			<div class="panel-heading">
				<h3 class="panel-title">
					<b><spring:message code="difflist.title2" /></b>
				</h3>
			</div>
			<c:if test="${empty mapStyleChanges}">
				<div class="panel-body"><spring:message code="difflist.table2.empty" /></div>
			</c:if>
			<c:if test="${not empty mapStyleChanges}">
				<table class="table table-striped">
					<thead>
						<tr>
							<th><spring:message code="difflist.table2.head1" /></th>
							<th><spring:message code="difflist.table2.head2" /></th>
							<th><spring:message code="difflist.table2.head3" /></th>
							<th><spring:message code="difflist.table2.head4" /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="result" items="${mapStyleChanges}">
							<tr>
								<td>${result.key.getName()}</td>
								<td>${result.value[0]}</td>
								<td>${result.value[1]}</td>
								<c:if test="${result.value[0] gt result.value[1]}">
									<td class="danger">-${result.value[2]}</td>
								</c:if>
								<c:if test="${result.value[0] eq result.value[1]}">
									<td class="active">${result.value[2]}</td>
								</c:if>
								<c:if test="${result.value[0] lt result.value[1]}">
									<td class="success">+${result.value[2]}</td>
								</c:if>
								
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div id='colchart_diff' ></div>
			</c:if>

		</div>
		<!-- 
		<div class="panel panel-success">
			<div class="panel-heading">
				<h3 class="panel-title">
					<b>Parrafo</b>
				</h3>
			</div>
			<div class="panel-body">
			<div id="wordtree_explicit" style="width: 900px; height: 500px;"></div>
			</div>
		</div>
		 -->
	</div>


	<script src="<c:url value="/resources/js/jquery-2.2.4.js"/>"></script>
	<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
	 <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
  <script type="text/javascript">
    google.charts.load('current', {'packages':['corechart'], 'language': '<spring:message code="index.languageSelector.active" />'});
    google.charts.setOnLoadCallback(drawChart);
  function drawChart() {
	var oldData = new google.visualization.DataTable();
	oldData.addColumn('string', '<spring:message code="difflist.drawChart.colum1" />');
	oldData.addColumn('number', '<spring:message code="difflist.drawChart.colum2" />');

	var newData = new google.visualization.DataTable();
	newData.addColumn('string', '<spring:message code="difflist.drawChart.colum3" />');
	newData.addColumn('number', '<spring:message code="difflist.drawChart.colum4" />');

	<c:if test="${not empty mapStyleChanges}">
	
	<c:forEach var="result" items="${mapStyleChanges}">                                                   
      oldData.addRow(['${result.key.getName()}', <c:out value="${result.value[0]}" />]);
    </c:forEach>

	<c:forEach var="result" items="${mapStyleChanges}">                                                   
		newData.addRow(['${result.key.getName()}', <c:out value="${result.value[1]}" />]);
    </c:forEach>

    </c:if>

    var colChartDiff = new google.visualization.ColumnChart(document.getElementById('colchart_diff'));


    var options = { legend: { position: 'top' } ,vAxis : {'format' : 'short'},
    		diff: { oldData: { opacity: 1.0,  title: '<spring:message code="difflist.drawChart.label1" />' },newData: { opacity: 0.5, }  } ,
 
	};



    var diffData = colChartDiff.computeDiff(oldData, newData);
    colChartDiff.draw(diffData, options);

  }
</script>
	
	
	
	<!--    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
      google.charts.load('current', {packages:['wordtree']});
      google.charts.setOnLoadCallback(drawSimpleNodeChart);
      function drawSimpleNodeChart() {
        var nodeListData = new google.visualization.arrayToDataTable([
          ['id', 'childLabel', 'parent', 'size', { role: 'style' }],
          [0, 'YO<sup>1</sup>+YO<sup>2</sup>==YO<sup>3</sup>==', -1, 1, 'black'],
          [1, 'YO', 0, 1, 'black'],
          [2, '<sup> </sup>', 0, 1, 'green'],
          [3, '+YO', 0, 1, 'black'],
          [4, '<sup> </sup>', 0, 1, 'green'],
          [5, '== ==', 0, 1, 'black'],
          [6, '1', 2, 1, 'black'],
          [7, '2', 4, 1, 'black'],
          [8, 'YO', 5, 1, 'black'],
          [9, '<sup> </sup>', 5, 1, 'black'],
          [10, '3', 9, 1, 'green']

          ]);

        var options = {
          colors: ['black', 'black', 'black'],
          wordtree: {
            format: 'explicit',
            type: 'suffix'
          }
        };

        var wordtree = new google.visualization.WordTree(document.getElementById('wordtree_explicit'));
        wordtree.draw(nodeListData, options);
      }
    </script>-->
</body>
</html>