
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<form class="navbar-form navbar-right" action='${pageContext.request.contextPath}/Login' method="POST">
    <div class="form-group">
      <!-- Username -->
      <label class="form-label sr-only"  for="username">Username</label>
      <div class="col-xs-10 col-xs-offset-2">
        <input type="text" id="username" name="username" placeholder="Username" class="form-control">
      </div>
    </div>
 
    <div class="form-group">
      <!-- Password-->
      <label class="form-label sr-only" for="password">Password</label>
      <div class="col-xs-10 col-xs-offset-1">
        <input type="password" id="password" name="password" placeholder="Password" class="form-control">
      </div>
    </div>
 	
    <div class="form-group">
      <!-- Button -->
      <div class="col-xs-11">
        <button class="btn btn-success">Login</button>
      </div>
    </div>
</form>