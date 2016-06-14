<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="tag" uri="/WEB-INF/tag/paginationTag.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Lista de Paginas</title>
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
						<b>Paginas</b>
					</div>

				</div>
				<div align="right">
					<a class="btn btn-primary" href="/WikiWebTest/" role="button">Atras</a>
				</div>
			</div>

			<c:if test="${empty pages}">
				<div class="panel-body">No hay Paginas</div>
			</c:if>
			<c:if test="${not empty pages}">

				<table class="table table-stripped">
					<thead>
						<tr>
							<th>Id</th>
							<th>Titulo</th>
							<th>Namespace</th>
							<th>Nº de Reviciones</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${pages}" var="page" varStatus="itr">
							<tr>
								<td>${page.getId() }</td>
								<td>${page.getTitle() }</td>
								<td>${page.getNs() }</td>
								<td><a href="getAllRevisionsOf?parentId=${page.getId() }"
									class="btn btn-primary" type="button"> Ver <span
										class="badge">${page.getRevisions().size() }</span>
								</a></td>
								<td><a href="statisticsPageOf?parentId=${page.getId() }"
									class="btn btn-primary" type="button"> Estadisticas </a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<tag:paginate max="15" offset="${offset}" count="${count}"
					uri="listPages" next="&raquo;" previous="&laquo;" />

			</c:if>

		</div>
	</div>


	<script src="<c:url value="/resources/js/jquery-2.2.4.js"/>"></script>
	<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
</body>
</html>