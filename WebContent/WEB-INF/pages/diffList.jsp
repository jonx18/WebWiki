<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Diff</title>
<!-- Bootstrap CSS -->
<link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet">
</head>
<body class=".container-fluid">
	<div class="container myrow-container">
		<div class="panel panel-success">
			<div class="panel-heading">
				<h3 class="panel-title">
						<b>Diff</b>
				</h3>
			</div>
			<div class="panel-body">
				<c:if test="${empty diffResult}">
						No hay diff
				</c:if>
				<ul class="list-group">


				<c:if test="${not empty diffResult}">
				<c:forEach items="${diffResult}" var="diff">
				<div class="row">
					<div class="col-md-6">
					
						<c:if test="${diff[0].operation == 'EQUAL'}">
							<li class="list-group-item list-group-item-info"><c:out value="${diff[0].text}" /></li>
						</c:if>

						<c:if test="${diff[0].operation == 'DELETE'}">
							<li class="list-group-item list-group-item-danger"><c:out value="${diff[0].text}" /></li>
						</c:if>
					</div>
					<div class="col-md-6">
						<c:if test="${diff[1].operation == 'EQUAL'}">
							<li class="list-group-item list-group-item-info"><c:out value="${diff[1].text}" /></li>
						</c:if>
						<c:if test="${diff[1].operation == 'INSERT'}">
							<li class="list-group-item list-group-item-success"><c:out value="${diff[1].text}" /></li>
						</c:if>
					</div>
					</div>
					</c:forEach>
					
				</c:if>
				</ul>
			</div>
		</div>
		</div>


		<script src="<c:url value="/resources/js/jquery-2.2.3.js"/>"></script>
		<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
</body>
</html>