<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="dumptodb.titlepage" /></title>
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
						<b><spring:message code="dumptodb.title" /></b>
					</div>

				</div>
				<div align="right">
					<a class="btn btn-primary" href="/WikiWebTest/" role="button"><spring:message code="dumptodb.back" /></a>
				</div>
			</div>

			<c:if test="${empty result}">
				<div class="panel-body"><spring:message code="dumptodb.table.empty" /></div>
			</c:if>
			<c:if test="${not empty result}">
				<table class="table table-striped">
					<thead>
						<tr>
							<th><spring:message code="dumptodb.table.head1" /></th>
							<th><spring:message code="dumptodb.table.head2" /></th>
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


	<script src="<c:url value="/resources/js/jquery-2.2.4.js"/>"></script>
	<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
</body>
</html>