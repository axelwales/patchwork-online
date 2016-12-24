<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div data-username="<c:out value="${ player.username }" />" class="row update-player-board">
	<c:set var="board" value="${ player.board.state }" />
	
	<%@ include file="/WEB-INF/views/play/PlayerStats.jsp" %>
	
	<div class="col-sm-8">
		<div class="player-board-container panel panel-default" data-current-player="<c:out value="${ player.currentPlayer }" />">
			<c:forEach begin="0" end="8" varStatus="i">
				<div class="row">
				 <c:forEach begin="0" end="2" varStatus="j">
					<div class="col-xs-4">
					  <c:forEach begin="0" end="2" varStatus="k">
					  <!-- row is i, column is j*3+k -->
					  <div class="col-xs-4">
					  	<div data-occupied="<c:out value="${ board[j.index*3+k.index][8-i.index] }" />" class="player-board-square"></div>
					  </div>
					  </c:forEach>
					</div>
				</c:forEach>
				</div>
			</c:forEach>
		</div><!-- end .player-board-container -->
	</div>
	<div data-current-player="<c:out value="${ player.currentPlayer }" />" class="col-sm-2 player-actions current-player place-commands">
		<div class="place-buttons-container btn-group-vertical" role="group" aria-label="board-actions">
			<div data-action="place" class="btn btn-primary action-button" aria-label="board-actions">Place</div>
			<div data-action="rotate" class="btn btn-primary action-button" aria-label="board-actions">Rotate</div>
			<div data-action="flip" class="btn btn-primary action-button" aria-label="board-actions">Flip</div>
			<div data-action="ai" class="btn btn-primary action-button" aria-label="choice-buttons"><c:out value="Use MCTS AI"/></div>
		</div>
	</div>
</div> <!-- end row -->
