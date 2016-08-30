<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="tag" uri="/WEB-INF/tag/paginationTag.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${page.getTitle() }</title>
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
						<b><spring:message code="revisionlistof.title" /></b>
					</div>

				</div>
				<div align="right">
					<a class="btn btn-primary" href="javascript:window.history.back();" role="button">
					<spring:message code="revisionlistof.back" /></a>
				</div>
			</div>

			<c:if test="${empty revisions}">
				<div class="panel-body"><spring:message code="revisionlistof.table.empty" /></div>
			</c:if>
			<c:if test="${not empty revisions}">
			
			  <table class="table table-stripped">
			  <thead>
				   <tr>
				    <th><spring:message code="revisionlistof.table.head1" /></th>
				    <th><spring:message code="revisionlistof.table.head2" /></th>
				    <th><spring:message code="revisionlistof.table.head3" /></th>
				    <th><spring:message code="revisionlistof.table.head4" /></th>
				    <th><spring:message code="revisionlistof.table.head5" /></th>
				    <th><spring:message code="revisionlistof.table.head6" /></th>
				   </tr>
				   </thead>
				   <tbody>
				   <c:forEach items="${revisions}" var="revision" varStatus="itr">
				    <tr>
				     <td>${revision.getId() }</td>
				     <td>${revision.getTimestamp() }</td>
				     <c:if test="${not revision.getContributor().getDeleted()}">
			    	 <c:if test="${revision.getContributor().getRealId()>=0}">
				     	<td>${revision.getContributor().getUsername() }</td>
				     </c:if>
				     <c:if test="${revision.getContributor().getRealId()<0}">
				     	<td><spring:message code="revisionlistof.table.value1" /> - ${revision.getContributor().getIp() }</td>
				     </c:if>
				     </c:if>
					 <c:if test="${revision.getContributor().getDeleted()}">
					 <td><spring:message code="revisionlistof.table.value2" /></td>
					 </c:if>
				     <c:if test="${revision.getMinor()}">
				     	<td><spring:message code="revisionlistof.table.value3" /></td>
				     </c:if>
				     <c:if test="${not revision.getMinor()}">
				     	<td><spring:message code="revisionlistof.table.value4" /></td>
				     </c:if>
					 <td>${revision.getComment() }</td>
					 <c:if test="${not empty revision.getParentid()}">
				     	<td>
				     	<a href="diffList?id=${ revision.getId()}&parentId=${ revision.getParentid()}"  class="btn btn-primary" type="button">
					  		<spring:message code="revisionlistof.table.button1" /><span class="badge">${ revision.getParentid()}</span>
						</a>
				     	</td>
				     </c:if>
				    </tr>
				   </c:forEach>
				   </tbody>
				  </table>
				  <tag:paginate max="15" offset="${offset}" count="${count}"
				   uri="getAllRevisionsOf" parentId="${page.getId() }" next="&raquo;" previous="&laquo;" />
			
			</c:if>

		</div>
	</div>


	<script src="<c:url value="/resources/js/jquery-2.2.4.js"/>"></script>
	<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
</body>
</html>