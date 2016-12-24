<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="create-game-container">
	<div class="col-xs-12 page-header">
		<h3>Create Game</h3>
	</div>
	<div class="col-xs-12">
		<div id="create-game-form">
			<form action="Create" method="post">
			  <div class="form-group">
			    <label for="gamename">Game Name</label>
			    <input type="text" class="form-control" id="gamename" name ="gamename" placeholder="Input Name">
			  </div>
			  <button type="submit" class="btn btn-default">Create</button>
			</form>
		</div>
	</div>
</div>
