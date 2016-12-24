<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="track" value="${game.state.track}" scope="request"/>
<c:set var="income" value="${requestScope.constants.income}" scope="request"/>
<c:set var="bonuses" value="${requestScope.constants.bonuses}" scope="request"/>
<c:set var="incomeOrBonus" value="" scope="request"/>
<c:set var="playerMarker" value="" scope="request"/>
<div class="row">
<div class="col-xs-12">
	<div class="time-track-container">
		<c:forEach begin="0" end="53" varStatus="i">
			<c:forEach items="${game.players}" var="player" >
		    </c:forEach>
			<c:choose>
				<c:when test="${income.contains(i.index)}">
		       		<c:set var="incomeOrBonus" value="time-track-income" scope="request"/>
				</c:when>
		        <c:when test="${bonuses.contains(i.index) && i.index >= game.state.track.nextUnclaimed}">
		        	<c:set var="incomeOrBonus" value="time-track-bonus" scope="request"/>
		        </c:when>
		        <c:otherwise>
		        	<c:set var="incomeOrBonus" value="" scope="request"/>
				</c:otherwise>
		    </c:choose>
			<div class="col-xs-1">
			  <div data-position="<c:out value='${i.index}'/>" id="time-track-square-<c:out value='${i.index}' />" class="time-track-square <c:out value='${incomeOrBonus}'/>"></div>
			</div>
		</c:forEach>
	</div><!-- end .time-track-container -->
</div>
</div>