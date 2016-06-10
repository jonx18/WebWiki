<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet">
<title>Informacion de Revision</title>
</head>
<body>
<body class=".container-fluid">
	<div class="container myrow-container">
		<div class="panel panel-success">
			<div class="panel-heading">
				<h3 class="panel-title">Informacion de Revision</h3>
			</div>
			<div class="panel-body">
				<form:form id="revisionRegisterForm" cssClass="form-horizontal"
					modelAttribute="revision" method="post" action="saveRevision">

					<div class="form-group">
						<div class="control-label col-xs-3">
							<form:label path="id">Id</form:label>
						</div>
						<div class="col-xs-6">

							<form:input cssClass="form-control" path="id"
								value="${revisionObject.id}" />
						</div>
					</div>
					<div class="form-group">
						<div class="control-label col-xs-3">
							<form:label path="parentid">Parent ID</form:label>
						</div>
						<div class="col-xs-6">
							<form:input cssClass="form-control" path="parentid"
								value="${revisionObject.parentid}" />
						</div>
					</div>

					<div class="form-group">
						<div class="control-label col-xs-3">
							<form:label path="contributor.id">Contributor Id</form:label>
						</div>
						<div class="col-xs-6">
							<form:input cssClass="form-control" path="contributor.id"
								value="${revisionObject.contributor.id}" />
						</div>
					</div>
					<div class="form-group">
						<div class="control-label col-xs-3">
							<form:label path="contributor.username">Contributor Username</form:label>
						</div>
						<div class="col-xs-6">
							<form:input cssClass="form-control" path="contributor.username"
								value="${revisionObject.contributor.username}" />
						</div>
					</div>
					<div class="form-group">
						<div class="control-label col-xs-3">
							<form:label path="comment">Comment</form:label>
						</div>
						<div class="col-xs-6">
							<form:input cssClass="form-control" path="comment"
								value="${revisionObject.comment}" />
						</div>
					</div>
					<div class="form-group">
						<div class="control-label col-xs-3">
							<form:label path="model">Model</form:label>
						</div>
						<div class="col-xs-6">
							<form:input cssClass="form-control" path="model"
								value="${revisionObject.model}" />
						</div>
					</div>
					<div class="form-group">
						<div class="control-label col-xs-3">
							<form:label path="format">Format</form:label>
						</div>
						<div class="col-xs-6">
							<form:input cssClass="form-control" path="format"
								value="${revisionObject.format}" />
						</div>
					</div>
					<div class="form-group">
						<div class="control-label col-xs-3">
							<form:label path="text">Text</form:label>
						</div>
						<div class="col-xs-6">
							<form:input cssClass="form-control" path="text"
								value="${revisionObject.text}" />
						</div>
					</div>
					<div class="form-group">
						<div class="control-label col-xs-3">
							<form:label path="sha1">Sha1</form:label>
						</div>
						<div class="col-xs-6">
							<form:input cssClass="form-control" path="sha1"
								value="${revisionObject.sha1}" />
						</div>
					</div>

					<div class="form-group">
						<div class="row">
							<div class="col-xs-4"></div>
							<div class="col-xs-4">
								<input type="submit" id="saveRevision" class="btn btn-primary"
									value="Save" onclick="return submitRevisionForm();" />
							</div>
							<div class="col-xs-4"></div>
						</div>
					</div>

				</form:form>
			</div>
		</div>
	</div>
	

	<script src="<c:url value="/resources/js/jquery-2.2.4.js"/>"></script>
	<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
	<script type="text/javascript">
		function submitEmployeeForm() {

/* 			// getting the employee form values
			var name = $('#name').val().trim();
			var age = $('#age').val();
			var salary = $('#salary').val();
			if (name.length == 0) {
				alert('Please enter name');
				$('#name').focus();
				return false;
			}

			if (age <= 0) {
				alert('Please enter proper age');
				$('#age').focus();
				return false;
			}

			if (salary <= 0) {
				alert('Please enter proper salary');
				$('#salary').focus();
				return false;
			} */
			return true;
		};
	</script>
</body>


</html>