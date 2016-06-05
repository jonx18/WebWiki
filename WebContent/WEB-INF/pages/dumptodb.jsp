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
			<div class="panel-body">
				<c:if test="${empty result}">
						No hay Tiempos
				</c:if>
				<c:forEach var="result" items="${result}">
  					 Operacion: ${result.key}
   						Tiempo en milisegundos: ${result.value} 
				</c:forEach>
			</div>
		</div>
		</div>


		<script src="<c:url value="/resources/js/jquery-2.2.3.js"/>"></script>
		<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
</body>
</html>