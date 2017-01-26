/**
 * 
 */

$(function() { 
	if($("#current-action-variable").data("game-over") == true) {
		displayScores();
	} else {
		if($("#current-player-boolean").data("current-player") == true) {
			showActions();
			if ($("#current-player-boolean").data("current-player-ai") == true) {
				var action = "ai";
				var params = '';
				actionAJAX(action,params);
			}
		}
		else {
			if ($("#single-player-boolean").data("single-player") == true) {
				console.log("Single Player!");
				var action = "ai";
				var params = '';
				actionAJAX(action,params);
			}
			else {
				console.log("Not Single Player!");
				doPoll($("#current-player-boolean").data("current-player-name"));
			}
		}
	}
});

function displayScores() {
	var scoreModal = $("<div></div>", {
		title:"Game Over!"
	});
	var contentContainer =  $('<div></div>', {
		class: "game-over-modal-content col-xs-8 col-xs-offset-2"
	});
	var winner = '';
	var maxScore = -100;
	var minScore = -100;
	var scoresContainer = $('<div></div>').append($('<p></p>').text('Final Score'));
	$('.player-stats').each( function() {
		var username = $(this).data('username');
		var buttons = $(this).data('buttons');
		if(buttons > maxScore) {
			minScore = maxScore;
			maxScore = buttons;
			winner = username;
		}
		var playerText = $('<p></p>').text(username + ': ' + buttons);
		scoresContainer.append(playerText);
	});
	var winnerText = $("<h3></h3>");
	if(maxScore > minScore)
		winnerText.text( winner + " wins!");
	else
		winnerText.text( "Tie Game!");
	contentContainer.append(winnerText);
	contentContainer.append(scoresContainer);
	scoreModal.append(contentContainer);
	scoreModal.dialog({
		  modal: true,
		  draggable: false,
		  resizable: false,
		  closeText: "Back to Game"
	});
}

function hideActions() {
	var actionSelector = '.' + $("#current-action-variable").data('action') + '-commands';
	$(actionSelector + '.current-player').removeClass('show');
}

function showActions() {
	var action = $("#current-action-variable").data('action');
	var actionSelector = '.' + action + '-commands';
	if ($("#current-player-boolean").data("current-player") == true) {
		if(action == 'place') {
			$(actionSelector + '.current-player').each(function() {
				if($(this).data('current-player') == true ) {
					$(this).toggleClass('show');
				}
			});
		}
		else
			$(actionSelector + '.current-player').toggleClass('show');
	}
	else
		hideActions();
	
	$('.action-button').each(function() {
		var action = $(this).data('action');
		var params = null;
		if(action == "flip") {
			$(this).unbind('click').on('click', function() {
				globals.boardPatch.flip();
			});
		}
		else if (action == "rotate") {
			$(this).unbind('click').on('click', function() {
				globals.boardPatch.rotate();
			});
		}
		else if (action == "choose") {
			params = 'choice:'+$(this).data('choice');
			$(this).unbind('click').on('click', function() {
				actionAJAX(action,params);
			});
		}
		else if (action == "ai") {
			params = '';
			$(this).unbind('click').on('click', function() {
				actionAJAX(action,params);
			});
		}
		else if (action == "place") {
			$(this).unbind('click').on('click', function() {
				var patch = globals.boardPatch;
				var patchParam = 'patch:'+ $("#current-action-variable").data('patch-id');
				var xParam = 'x:'+ patch.x;
				var yParam = 'y:'+ patch.y;
				var flipParam = 'flip:'+ patch.flipped;
				var rotationsParam = 'rotations:'+ patch.rotations;
				params = patchParam+';'+xParam+';'+yParam+';'+flipParam+';'+rotationsParam;
				actionAJAX(action,params);
			});
		}
	});
}

function actionAJAX(action,params) {
	$.ajax({
        url: '',
        type: 'POST',
        dataType: 'json',
        data: {"action":action, "params":params},
        error: function(jqXHR, textStatus, errorThrown){
            console.log("Error: " + textStatus);
            console.log(jqXHR);
        },
        success:function(data){
        	console.log(data);
        	if(data.success == true) {
        		updateGame(action, params, data);
        	}
        }
    });
};

function doPoll(username){
	if($("#current-action-variable").data("game-over") == false) {
		updateAll(username);
	} else {
		displayScores();
	}
}

function updateAll(username) {
	$.ajax({
        url: '',
        type: 'POST',
        dataType: 'json',
        data: {"action":"updatepoll", "username":username},
        error: function(jqXHR, textStatus, errorThrown){
            console.log("Error: " + textStatus);
            setTimeout(doPoll,5000,username);
        },
        success:function(data){
        	console.log(data);
        	if(data.success == true) {
        		$('#patchwork-game-container').replaceWith(data.container);
        		showPlayerBoard();
        		createPatchList(globals.patchlist);
        		showActions();
        		showTimeTrack();
        		setTabs();
        		if($("#current-action-variable").data("game-over") == true) {
        			displayScores();
        		} else if ($("#current-player-boolean").data("current-player-ai") == true) {
    				var name = "ai";
    				var params = '';
    				actionAJAX(name,params);
    			}
        	}
        	else {
        		setTimeout(doPoll,5000,username);
        	}
        }
    });
}

function updateGame(action, params, htmlData) {
	if(action == "choose" || action == "place") {
		var user = $('#current-player-boolean').data('current-player-name');
		$('#game-variables').replaceWith(htmlData.client); //updates current player variables
		var nextUser = $('#current-player-boolean').data('current-player-name');
		if (action == "choose") {
			$('#choose-buttons-container').parent().remove();
			$('.update-choose').replaceWith(htmlData.patches);
			$('.time-track-container').replaceWith(htmlData.time);
			$('.update-player-board[data-username='+user+'] .player-stats').replaceWith(htmlData.stats);
			showTimeTrack();
			createPatchList(globals.patchlist);
			addPlayerPatch(globals.patchlist);
		} else if (action == "place") {
			$('.update-player-board[data-username='+user+']').replaceWith(htmlData.board);
			showPlayerBoard();
		}
		
		if ($("#single-player-boolean").data("single-player") == true) {
			if(nextUser != user) {
				$('.update-player-board[data-username='+user+'] .player-stats').data('current-player', false);
				$('.update-player-board[data-username='+user+'] .player-board-container').data('current-player', false);
				$('.update-player-board[data-username='+user+'] .player-actions').data('current-player', false);
				$('.update-player-board[data-username='+user+'] .player-stats-current-player').text('current player: false');
				
				$('.update-player-board[data-username='+nextUser+'] .player-stats').data('current-player', true);
				$('.update-player-board[data-username='+nextUser+'] .player-board-container').data('current-player', true);
				$('.update-player-board[data-username='+nextUser+'] .player-actions').data('current-player', true);
				$('.update-player-board[data-username='+nextUser+'] .player-stats-current-player').text('current player: true');
			}
		}
		
		if($("#current-player-boolean").data("current-player") == true) {
			showActions();
			if ($("#current-player-boolean").data("current-player-ai") == true) {
				var name = "ai";
				var params = '';
				actionAJAX(name,params);
			}
		} else {
			hideActions();
			if ($("#single-player-boolean").data("single-player") == false) 
				doPoll($('#current-player-boolean').data('current-player-name'));
			else {
				var name = "ai";
				var params = '';
				actionAJAX(name,params);
			}
		}

	} else if (action == "ai") {
		actionAJAX(htmlData.commandName,htmlData.commandParams);
	}
}