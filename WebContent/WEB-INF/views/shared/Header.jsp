<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>
	<c:out value='${requestScope.headTitle}'/>
</title>
	<c:set var="context" value="${pageContext.request.contextPath}" />
	<link href="<c:out value='${context}'/>/static/css/bootstrap.css" rel="stylesheet" type="text/css">
	<link href="<c:out value='${context}'/>/static/css/jquery-ui.css" rel="stylesheet" type="text/css">
	<link href="<c:out value='${context}'/>/static/css/site.css" rel="stylesheet" type="text/css">
	<c:forEach items="${requestScope.cssFiles}" var="file">
	<link href="<c:out value='${context}'/>/static/css/<c:out value='${file}'/>.css" rel="stylesheet" type="text/css">
	</c:forEach>
</head>

<body>
<%@ include file="/WEB-INF/views/shared/NavBar.jsp" %>
<div id="content">