/**
 * 
 */
function showPlayerBoard() {
    $(".player-board-container").each(function () {
    	$(this).css({
	        height: $(this).outerWidth()
	    });
	    var totalWidth = $(this).outerWidth();
	    var totalHeight = $(this).outerHeight();
	    $(this).find(".player-board-square").css({
	        width: totalWidth/9 + "px",
	        height: totalHeight/9 + "px"
	    });
	    $(this).find(".player-board-square").each(function() {
	    	var occupied = $(this).data('occupied');
	    	if(occupied == 1) {
	    		$(this).toggleClass('square-occupied',true);
	    	}
	    });
    });
	if($("#current-player-boolean").data("current-player") == true && globals.patchlist != null)
		addPlayerPatch(globals.patchlist);
};

$(function() {
	showPlayerBoard();
});