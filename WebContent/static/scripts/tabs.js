/**
 * 
 */

$('#game-tabs a').click(function (e) {
  $(this).tab('show');
});

$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
	  e.target; // newly activated tab
	  e.relatedTarget; // previous active tab
	  
	  var display = $(this).data('display');
	  if(display == 'player-board')
		  showPlayerBoard();
	  else
		  showTimeTrack();

});

function setTabs() {
	$('#game-tabs a').click(function (e) {
		  $(this).tab('show');
		});

		$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
			  e.target; // newly activated tab
			  e.relatedTarget; // previous active tab
			  
			  var display = $(this).data('display');
			  if(display == 'player-board')
				  showPlayerBoard();
			  else
				  showTimeTrack();

		});
}