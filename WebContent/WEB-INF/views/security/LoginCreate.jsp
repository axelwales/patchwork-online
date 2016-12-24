<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<form class="form-horizontal" action='Register' method="POST">
  <div id="legend">
      <legend class=""><h1>Register</h1></legend>
    </div>
    
    <div class="form-group">
      <!-- Username -->
      <label class="form-label col-xs-2"  for="username">Username</label>
      <div class="col-xs-10">
        <input type="text" id="username" name="username" placeholder="Enter Username" class="form-control">
        <p class="help-block">Username Help Text</p>
      </div>
    </div>
    
    <!--  <div class="control-group">
      <label class="control-label" for="email">E-mail</label>
      <div class="controls">
        <input type="text" id="email" name="email" placeholder="" class="input-xlarge">
        <p class="help-block">Please provide your E-mail</p>
      </div>
    </div> -->
 
    <div class="form-group">
      <!-- Password-->
      <label class="form-label col-xs-2" for="password">Password</label>
      <div class="col-xs-10">
        <input type="password" id="password" name="password" placeholder="Enter Password" class="form-control">
        <p class="help-block">Password help text</p>
      </div>
    </div>
 
   <div class="form-group">
      <!-- Password-->
      <label class="form-label col-xs-2" for="password">Confirm<br>Password</label>
      <div class="col-xs-10">
        <input type="password" id="password_confirm" name="password_confirm" placeholder="Confirm Password" class="form-control">
        <p class="help-block">Please confirm password</p>
      </div>
    </div>
 
    <div class="form-group">
      <!-- Button -->
      <div class="col-xs-9 col-xs-offset-3">
        <button class="btn btn-success">Register</button>
      </div>
    </div>
</form>

<%@ include file="/WEB-INF/views/shared/Footer.jsp" %>