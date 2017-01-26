<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="patchwork-game-container">

<%@ include file="/WEB-INF/views/play/ClientVariables.jsp" %>

<div class="row">
<ul id="game-tabs" class="nav nav-tabs" role="tablist">
  <c:forEach items="${ game.players }" var="player" >
    <c:set var="active" value="" />
    <c:if test="${ player.currentPlayer == true }"><c:set var="active" value="active" /></c:if>
    <li role="presentation" class="<c:out value="${ active }" />">
      <a data-display="player-board" href="#<c:out value="${ player.username }" />" aria-controls="<c:out value="${ player.username }" />" role="tab" data-toggle="tab"><c:out value="${ player.username }" /></a>
    </li>
  </c:forEach>
  <li role="presentation"><a data-display="time-track" href="#timetrack" aria-controls="timetrack" role="tab" data-toggle="tab">Time Track</a></li>
  <li role="presentation"><a data-display="mcts" href="#mcts" aria-controls="mcts" role="tab" data-toggle="tab">MCTS Tree</a></li>
</ul>
</div>

<div class="row">
<div class="tab-content">
  <c:forEach items="${ game.players }" var="player" >
    <c:set var="active" value="" />
    <c:if test="${ player.currentPlayer == true }"><c:set var="active" value="active" /></c:if>
    <div role="tabpanel" class="tab-pane <c:out value="${ active }" />" id="<c:out value="${ player.username }" />">
    	<%@ include file="/WEB-INF/views/play/PlayerBoard.jsp" %>
    </div>
  </c:forEach>
  <div role="tabpanel" class="tab-pane" id="timetrack">
    <div class="row">
	  <div class="col-xs-12">
  	    <%@ include file="/WEB-INF/views/play/TimeTrack.jsp" %>
  	  </div>
	</div>
  </div>
  <div role="tabpanel" class="tab-pane" id="mcts">
  	<%@ include file="/WEB-INF/views/play/MCTSTree.jsp" %>
  </div>
</div>
</div>

<%@ include file="/WEB-INF/views/play/PatchList.jsp" %>

</div>
