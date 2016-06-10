<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Lista de Revisiones</title>
<!-- Bootstrap CSS -->
<link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet">
</head>
<body class=".container-fluid">
	<div class="container myrow-container">
		<div class="panel panel-success">
			<div class="panel-heading">
				<h3 class="panel-title">
					<div align="left">
						<b>Lista de Revisiones</b>
					</div>
					<div align="right">
						<a href="createRevision">Agregar Revision</a>
					</div>
				</h3>
			</div>
			<div class="panel-body">
				<c:if test="${empty revisionList}">
						No hay Revisiones
				</c:if>
				<c:if test="${not empty revisionList}">

					<table class="table table-hover table-bordered">
						<thead style="background-color: #bce8f1;">
							<tr>
								<th>Id</th>
								<th>Parent Id</th>
								<th>TimeStamp</th>
								<th>Contributor</th>
								<th>Comment</th>
								<th>Model</th>
								<th>Format</th>
								<th>Text</th>
								<th>Sha1</th>
								<th>Edit</th>
								<th>Delete</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${revisionList}" var="rev">
								<tr>
									<th><c:out value="${rev.id}" /></th>
									<th><c:out value="${rev.parentid}" /></th>
									<th><c:out value="${rev.timestamp}" /></th>
									<th><c:out value="${rev.contributor.id} ${rev.contributor.username}" /></th>
									<th><c:out value="${rev.comment}" /></th>
									<th><c:out value="${rev.model}" /></th>
									<th><c:out value="${rev.format}" /></th>
									<th><c:out value="${rev.text}" /></th>
									<th><c:out value="${rev.sha1}" /></th>
									<th><a href="editRevision?id=<c:out value='${rev.id}'/>">Editar</a></th>
									<th><a href="deleteRevision?id=<c:out value='${rev.id}'/>">Eliminar</a></th>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:if>
			</div>
		</div>
		</div>
					<div align="right">
						<a href="showDiff">Agregar Revision</a>
					</div>

		<script src="<c:url value="/resources/js/jquery-2.2.4.js"/>"></script>
		<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
</body>
</html>