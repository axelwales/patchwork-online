<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div data-username="<c:out value="${ player.username }" />" 
	data-buttons="<c:out value="${ player.buttons }" />" 
	data-current-player="<c:out value="${ player.currentPlayer }" />" 
	data-position="<c:out value="${ player.position }" />" 
	class="col-sm-2 player-stats">
		<p>username: <c:out value="${ player.username }" /></p>
		<p>current player: <c:out value="${ player.currentPlayer }" /></p>
		<p>buttons: <c:out value="${ player.buttons }" /></p>
		<p>income: <c:out value="${ player.income }" /></p>
		<p>time track position: <c:out value="${ player.position }" /></p>
</div>