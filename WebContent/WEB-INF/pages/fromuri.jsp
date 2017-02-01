<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="fromuri.titlepage" /></title>
<!-- Bootstrap CSS -->
<link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet">
</head>
<body class=".container-fluid">
	<div class="col-md-2"></div>
	<div class="col-md-8">
		<div class="jumbotron">
			<div class="container">
					<h1><spring:message code="fromuri.title" /></h1>
					<p><spring:message code="fromuri.instruction" /></p>
					<p><spring:message code="fromuri.urlexample.1" />
					<small><spring:message code="fromuri.urlexample.2" />
					<strong><spring:message code="fromuri.urlexample.3" /></strong>
					</small></p>
						<spring:url value="/urltobd" var="urltobd" />
						<form method="post" id="verify" action='<c:out value = "urlToBDWithRedirection" />  '>
							<div class="form-group">
					        <textarea name="pagename" rows="6" cols="50" placeholder='<spring:message code="fromuri.example" />'></textarea>
					        </div>
					        <div class="checkbox">
						        <label for="statistics">
						        <input name="statistics" type="checkbox" value='<spring:message code="fromdump.statistics" />' >
						        <spring:message code="fromuri.statistics" /> 
						        </label>
					    	</div>
					        <c:out value = "${pagenameError}" />            
					        <input class="btn btn-primary btn-lg" type="submit" value='<spring:message code="fromuri.urltobd" />'
						role="button"/>     
					    </form>
					<p>
						<a class="btn btn-primary btn-lg" href="index" 
						role="button"><spring:message code="fromuri.back" /></a>
					</p>

			</div>
		</div>
	</div>

	<script src="<c:url value="/resources/js/jquery-2.2.4.js"/>"></script>
	<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
</body>
</html>