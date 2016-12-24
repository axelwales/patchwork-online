<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="cssFiles" value="${{'playerboard'}}" scope="request"/>
<c:set var="headTitle" value="${'Patchwork Online'}" scope="request"/>
<%@ include file="/WEB-INF/views/shared/Header.jsp" %>

<div class="col-xs-6">
	<div id="info-container jumbotron">
		<h1 class="text-center">Welcome<br>to<br>Patchwork Online</h1>
		<h3 class="text-center">An online implementation of the board game Patchwork</h3>
	</div>
</div>
	<div class="col-xs-6">
	<div id="register-container">
	<%@ include file="/WEB-INF/views/security/LoginCreate.jsp" %>
	</div>
</div>
<%@ include file="/WEB-INF/views/shared/Footer.jsp" %>