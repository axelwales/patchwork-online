<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<li class="game-list-item list-group-item row">
	<div class="col-xs-7">
		<div class="col-xs-12">
			<h4><c:out value='${listItem.name}'/></h4>
		</div>
		<c:set var="hasJoined" value="false" scope="request"></c:set>
		<c:forEach items="${listItem.players}" var="player">
		<c:if test="${player.username eq pageContext.request.userPrincipal.name || player.username eq 'Player' }">
			<c:set var="hasJoined" value="true" scope="request"></c:set>
		</c:if>
		<div class="col-xs-6">
			<span><c:out value='${player.username}'/></span>
		</div>
		</c:forEach>
	</div>
	
	<div class="col-xs-5">
	<c:set var="plength" value='${ fn:length(listItem.players) }' scope="request"></c:set>
	<c:choose>
	<%-- Allow player to join game --%>
    <c:when test="${hasJoined != true && plength < 2}">
		<form action='Join' method="POST">
			<input type="hidden" name="gameid" value="<c:out value='${listItem.id}'/>">
		    <div class="form-group">
		      <!-- Button -->
		      <div class="col-xs-12">
		        <button class="btn btn-primary"><c:out value='Join'/></button>
		      </div>
		    </div>
	  	</form>
	</c:when>
	<%-- Wait for a second player --%>
	<c:when test="${hasJoined == true && plength < 2}">
	    <h5>Waiting for Another Player...</h5>
    </c:when>
    <%-- Start game --%>
    <c:when test="${hasJoined == true && plength == 2 && listItem.isstarted != 1}">
		<form action='Start' method="POST">
			<input type="hidden" name="gameid" value="<c:out value='${listItem.id}'/>">
		    <div class="form-group">
		      <!-- Button -->
		      <div class="col-xs-12">
		        <button class="btn btn-primary"><c:out value='Start'/></button>
		      </div>
		    </div>
	  	</form>
	</c:when>
	<%-- Go to Game --%>
    <c:otherwise>
		<form action='Play/<c:out value='${listItem.id}'/>' method="POST">
			<input type="hidden" name="gameid" value="<c:out value='${listItem.id}'/>">
		    <div class="form-group">
		      <!-- Button -->
		      <div class="col-xs-12">
		        <button class="btn btn-primary"><c:out value='Go to Game'/></button>
		      </div>
		    </div>
	  	</form>
    </c:otherwise>
	</c:choose> <%-- end choose --%>
	</div>
</li>