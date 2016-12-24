<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="cssFiles" value="${{'lobby'}}" scope="request"/>
<c:set var="headTitle" value="${'Lobby'}" scope="request"/>
<%@ include file="../shared/Header.jsp" %>

<%@ include file="CreateGame.jsp" %>
<%@ include file="YourGames.jsp" %>
<%@ include file="AvailableGames.jsp" %>

<%@ include file="../shared/Footer.jsp" %>