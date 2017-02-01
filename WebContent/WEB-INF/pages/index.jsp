<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
						<a class="btn btn-primary btn-lg" href="listPages" role="button"><spring:message
								code="index.listpages" /> </a>
					</p>
					<p>
						<a class="btn btn-primary btn-lg" href="statisticsReviews"
							role="button"><spring:message code="index.statisticsReviews" /> </a>
					</p>
										<p>
						<a class="btn btn-primary btn-lg" href="statisticsPages"
							role="button"><spring:message code="index.statisticsPages" /> </a>
					</p>
					<p>
						<a class="btn btn-primary btn-lg" href="fromuri" 
						role="button"><spring:message code="index.fromuri" /></a>
					</p>
					<p>
						<a class="btn btn-primary btn-lg" href="exportall" 
						role="button"><spring:message code="index.exportall" /></a>
					</p>
					<p>
						<a class="btn btn-primary btn-lg" href="wikidrop" 
						role="button"><spring:message code="index.wikidrop" /></a>
					</p>
				</c:if>
				<c:if test="${empty mediawiki }">
					<h1><spring:message code="index.notWiki.title" /></h1>
					<p>
						<a class="btn btn-primary btn-lg" href="fromdump" 
						role="button"><spring:message code="index.notWiki.fromdump" /></a>
					</p>
					<p>
						<a class="btn btn-primary btn-lg" href="fromuri" 
						role="button"><spring:message code="index.fromuri" /></a>
					</p>
				</c:if>
					<select id="languageSelector" class="form-control">
					  <option value="es"><spring:message code="index.languageSelector.es" /></option>
					  <option value="en"><spring:message code="index.languageSelector.en" /></option>
					</select>

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