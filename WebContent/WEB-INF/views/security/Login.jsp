<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="cssFiles" value="${{'playerboard'}}" scope="request"/>
<c:set var="headTitle" value="${'Login'}" scope="request"/>
<%@ include file="/WEB-INF/views/shared/Header.jsp" %>

<form class="form-horizontal" action='Login' method="POST">
    <div id="legend">
      <legend class="">Login</legend>
    </div>
    <div class="form-group">
      <!-- Username -->
      <label class="form-label col-xs-1"  for="username">Username</label>
      <div class="col-xs-11">
        <input type="text" id="username" name="username" placeholder="Enter Username" class="form-control">
      </div>
    </div>
 
    <div class="form-group">
      <!-- Password-->
      <label class="form-label col-xs-1" for="password">Password</label>
      <div class="col-xs-11">
        <input type="password" id="password" name="password" placeholder="Enter Password" class="form-control">
      </div>
    </div>
 
    <div class="form-group">
      <!-- Button -->
      <div class="col-xs-11 col-xs-offset-1">
        <button class="btn btn-success">Login</button>
      </div>
    </div>
</form>

<%@ include file="/WEB-INF/views/shared/Footer.jsp" %>