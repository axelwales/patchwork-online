<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="row update-choose">
	<div id="choose-buttons-container" class="current-player choose-commands btn-group" role="group" aria-label="choice-buttons">
	<c:forEach begin="0" end="3" varStatus="i">
		<c:set var="choiceText" value="Choose Patch ${ i.index + 1 }" />
		<c:if test="${ i.index == '3' }" >
			<c:set var="choiceText" value="Get Buttons" />
		</c:if>
		<div data-action="choose" data-choice="<c:out value="${ i.index }"/>" class="btn btn-primary action-button" aria-label="choice-buttons">
			<c:out value="${ choiceText }"/>
		</div>
	</c:forEach>
		<div data-action="ai" class="btn btn-primary action-button" aria-label="choice-buttons">
			<c:out value="Use MCTS AI"/>
		</div>
	</div>
</div>
<c:set var="patchList" value="${game.state.patches.patches}" scope="request"/>
<div class="row update-choose">
<div id="patch-list-container">
	<c:forEach items="${patchList}" var="patch" varStatus="i">
		<div class="col-xs-2 panel panel-default">
		  <!-- row is i, column is j -->
		  <div data-position="<c:out value="${ i.index + 1 }"/>" id="p<c:out value="${ patch.staticId }"/>" class="patch-list-item"></div>
		</div>
	</c:forEach>
</div>
</div>