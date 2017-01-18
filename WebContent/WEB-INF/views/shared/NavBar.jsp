<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div id="nav-container">
    	<div id="nav-row">
		    <div class="navbar-header">
		      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#header-nav" aria-expanded="false">
		        <span class="sr-only">Toggle navigation</span>
		      </button>
		      <!--  <a class="navbar-brand" href="#"></a>  -->
		    </div>
		
		    <!-- Collect the nav links, forms, and other content for toggling -->
		    <div class="collapse navbar-collapse" id="header-nav">
		      <!--  <ul class="nav navbar-nav">
		        <li class="active"><a href="#">Link <span class="sr-only">(current)</span></a></li>
		        <li><a href="#">Link</a></li>
		      </ul> -->
		      <ul class="nav navbar-nav">
		        <li class="active"><a href="${pageContext.request.contextPath}/Play/Single">Play Offline</a></li>
		        <li class="active"><a href="${pageContext.request.contextPath}/Lobby">Lobby</a></li>
		      </ul>
		      <c:choose>
		    	<c:when test="${not empty pageContext.request.userPrincipal}">
		    	<div class="navbar-right">
					<%@ include file="/WEB-INF/views/security/LogoutNavBar.jsp" %>
				</div>
		    	</c:when>    
		    	<c:otherwise>
					<%@ include file="/WEB-INF/views/security/LoginNavBar.jsp" %>
		    	</c:otherwise>
			  </c:choose>
		
		    </div><!-- /#header-nav -->
        </div>
    </div>
  </div><!-- /.container-fluid -->
</nav>