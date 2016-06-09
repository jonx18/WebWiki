<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Wiki</title>
<!-- Bootstrap CSS -->
<link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet">
</head>
<body class=".container-fluid">
	<div class="col-md-2"></div>
	<div class="col-md-8">
		<div class="jumbotron">
			<div class="container">
				<c:if test="${not empty mediawiki }">
					<h1>${mediawiki.getSiteinfo().getSitename() }</h1>
					<p>
						<a class="btn btn-primary btn-lg" href="listPages" role="button">Lista
							de Paginas </a>
					</p>
					<p>
						<a class="btn btn-primary btn-lg" href="statisticsReviews"
							role="button">Estadisticas de revisiones </a>
					</p>
				</c:if>
				<c:if test="${empty mediawiki }">
					<h1>No hay Wiki Cargada</h1>
					<p>
						<a class="btn btn-primary btn-lg" href="dumptobd" role="button">Cargar
							Wiki</a>
					</p>
				</c:if>


			</div>
		</div>
	</div>

	<script src="<c:url value="/resources/js/jquery-2.2.3.js"/>"></script>
	<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
</body>
</html>