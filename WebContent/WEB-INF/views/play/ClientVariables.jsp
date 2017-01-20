<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="game-variables">
	<div id="json-url" data-json-url="<c:out value="${ pageContext.request.contextPath }"/>/JSON"></div>
	
	<div id="single-player-boolean" data-single-player="<c:out value='${ game.isSinglePlayer }' />"> </div>
	
	<c:set var="isCurrentPlayer" value="false" />
	<c:forEach items="${ game.players }" var="player" > 
		<c:if test="${ player.username == pageContext.request.userPrincipal.name || player.username eq 'Player' }" >
			<c:set var="isAI" value="${player.isAI == 1}" />
			<c:if test="${ player.currentPlayer }" >
				<c:set var="isCurrentPlayer" value="true" />
			</c:if>
		</c:if>
	</c:forEach>
	
	<div id="current-player-boolean" data-current-player="<c:out value="${ isCurrentPlayer }"/>" data-current-player-ai="<c:out value="${ isAI }"/>" data-current-player-name="<c:out value="${ pageContext.request.userPrincipal.name }"/>"></div>
	
	<c:set var="action" value="" />
	<c:set var="patch-variable" value="" />
	<c:set value="${ game.state.actionQueue[0].commands[0] }" var="command" />
	<c:choose>
		<c:when test="${ command.name == 'rotate' || command.name == 'flip' || command.name == 'place' }" >
			<c:set var="action" value="place" />
			<c:set var="patch-variable" value="p${ command.parameters['patch'] }" />
		</c:when>
		<c:when test="${ command.name == 'choose' }" >
			<c:set var="action" value="choose" />
			<c:set var="patch-variable" value="" />
		</c:when>
	</c:choose>
	
	<div id="current-action-variable" data-game-over="<c:out value="${ game.iscomplete == 1 }"/>" data-action="<c:out value="${ action }"/>" data-patch-id="<c:out value="${ requestScope.patchid }"/>"></div>
</div>