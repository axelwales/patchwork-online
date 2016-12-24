
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<form class="navbar-form navbar-right" action='${pageContext.request.contextPath}/Logout' method="POST">	
   <div class="form-group">
      <!-- Button -->      
     <div class="col-xs-11">
        <button class="btn btn-success">Logout</button>
     </div>
   </div>
</form>
<p class="navbar-text navbar-right">
	Hello, <c:out value="${pageContext.request.userPrincipal.name}" />!
</p>