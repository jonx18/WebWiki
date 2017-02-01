<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="error.titlepage" /></title>
<!-- Bootstrap CSS -->
<link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet">
</head>
<body class=".container-fluid">
	<div class="col-md-2"></div>
	<div class="col-md-8">
		<div class="jumbotron">
			<div class="container">
				
					<h1><spring:message code="error.title" /></h1>
					<c:if test="${not empty motivo }">
						<h2>${motivo }</h2>
					</c:if>

					
					
					<p>
						<a class="btn btn-primary btn-lg" href="index" 
						role="button"><spring:message code="error.back" /></a>
					</p>
			</div>
		</div>
	</div>

	<script src="<c:url value="/resources/js/jquery-2.2.4.js"/>"></script>
	<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
	<script type="text/javascript">
		$("#languageSelector").val("<spring:message code='index.languageSelector.active' />").attr('selected', true);;
		$("#languageSelector").change(function() {
			window.location.href = "?lang="+$( "#languageSelector option:selected" ).val();
		});
	</script>
</body>
</html>