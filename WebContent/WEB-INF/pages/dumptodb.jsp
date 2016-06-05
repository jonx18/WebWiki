<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Procesamiento</title>
<!-- Bootstrap CSS -->
<link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet">
</head>
<body class=".container-fluid">
	<div class="container myrow-container">
		<div class="panel panel-success">
			<div class="panel-heading">
				<h3 class="panel-title">
					<b>Procesamiento</b>
				</h3>
			</div>

			<c:if test="${empty result}">
				<div class="panel-body">No hay Tiempos</div>
			</c:if>
			<c:if test="${not empty result}">
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Operacion</th>
							<th>Tiempo en milisegundos</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="result" items="${result}">
							<tr>
								<td>${result.key}</td>
								<td>${result.value}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:if>

		</div>
	</div>


	<script src="<c:url value="/resources/js/jquery-2.2.3.js"/>"></script>
	<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
</body>
</html>