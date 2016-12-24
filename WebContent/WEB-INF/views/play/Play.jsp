<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="cssFiles" value="${{'playerboard','timetrack', 'patchlist', 'action'}}" scope="request"/>
<c:set var="jsFiles" value="${{'playerboard','timetrack','patch-manager','action-manager','tabs'}}" scope="request"/>
<c:set var="headTitle" value="${'Play Patchwork'}" scope="request"/>

<c:set var="game" value="${requestScope.game}" scope="request"/>

<%@ include file="/WEB-INF/views/shared/Header.jsp" %>

<%@ include file="/WEB-INF/views/play/GameContainer.jsp" %>

<%@ include file="/WEB-INF/views/shared/Footer.jsp" %>