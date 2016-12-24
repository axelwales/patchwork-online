<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="available-games-container">
	<div class="col-xs-12 page-header">
		<h3>Available Games</h3>
	</div>
	<div class="col-xs-12">
		<ul class="list-group game-list">
		<c:forEach items="${requestScope.availableGames}" var="game">
		<c:set var="listItem" value="${game}" scope="request"></c:set>
			<%@ include file="GameListItem.jsp" %>
		</c:forEach>
		</ul>
	</div>
</div>