<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="fromdump.titlepage" /></title>
<!-- Bootstrap CSS -->
<link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet">
</head>
<body class=".container-fluid">
	<div class="col-md-2"></div>
	<div class="col-md-8">
		<div class="jumbotron">
			<div class="container">
				
					<h1><spring:message code="fromdump.title" /></h1>
					<p><spring:message code="fromdump.instruction" /></p>
					<spring:url value="/dumptobd" var="dumptobd" />
						<form method="get" action='<c:out value = "dumptobd" />'>
						<div class="form-group">
							<label for="clave">
					        	<spring:message code="fromdump.clave" /> 
					        </label>
					        <input name="clave" type="text" placeholder='<spring:message code="fromdump.example" />' />
					    </div>
					    <div class="checkbox">
					        <label for="statistics">
					        <input name="statistics" type="checkbox" value='<spring:message code="fromdump.statistics" />' >
					        <spring:message code="fromdump.statistics" /> 
					        </label>
					    </div>

					    <div class="checkbox">
					        <label for="dinamic">
					        <input name="dinamic" type="checkbox" onchange='$("#limitdiv").toggle();$("#seeddiv").toggle()' value='<spring:message code="fromdump.dinamic" />' >
					        <spring:message code="fromdump.dinamic" /> 
					        </label>
					    </div>   
					   <div id="limitdiv" class="form-group" style="display:none;">
					        <label for="limit">
					        <spring:message code="fromdump.limit" /> 
					        </label>
					        <input name="limit" type="number" value='0' />
					        <p class="help-block"><spring:message code="fromdump.limit.help" /> </p>
					    

					    </div> 
					    <div id="seeddiv" class="form-group" style="display:none;">
					        <label for="seed">
					        <spring:message code="fromdump.seed" /> 
					        </label>
					        <input name="seed" type="number" value='7777' />
					        <p class="help-block"><spring:message code="fromdump.seed.help" /> </p>
					    </div> 
					        <c:out value = "${pagenameError}" />            
					        <input class="btn btn-primary btn-lg" type="submit" value='<spring:message code="fromdump.dumptobd" />'
						role="button"/>     
					    </form>
					
					<p>
						<a class="btn btn-primary btn-lg" href="index" 
						role="button"><spring:message code="fromdump.back" /></a>
					</p>

			</div>
		</div>
	</div>

	<script src="<c:url value="/resources/js/jquery-2.2.4.js"/>"></script>
	<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
	<script type="text/javascript">
	$( document ).ready(function() {
		$("#dinamic").change(function() {
			$("#limitdiv").toggle()
		});
	});
	</script>
</body>
</html>