/**
 * 
 */
function showTimeTrack() {
	var children = $(".time-track-square").children();
	if($(".time-track-square").children().length == 0) {
		$(".time-track-square").each(function() {
			var position = $(this).data("position");
			var numberDiv = $("<span></span>", {
				class: "small time-track-position",
			});
			if(position == 0)
				numberDiv.text("Start");
			else if(position == 53)
				numberDiv.text("End");
			else
				numberDiv.text(position);
			$(this).append(numberDiv);
		});
		
		var incomeDiv = $("<div></div>", {
			class: 'income-icon'
		});
		$(".time-track-income").append(incomeDiv);
		
		var bonusDiv = $("<div></div>", {
			class: 'bonus-icon'
		});
		$(".time-track-bonus").append(bonusDiv);
	}
	
	$(".time-track-square").each(function() {
		$(this).css({
			height: $(this).width() + "px"
		});
	});
	
	$("#time-track-square-0").parent().toggleClass("col-xs-offset-3", true);
	
	var parentHeight = $(".time-track-income").outerWidth();
	var parentWidth = $(".time-track-income").outerWidth();
	
	$(".income-icon").css({
		height: parentHeight/2 + "px",
		width: parentWidth/2 + "px",
		position: "absolute",
		top: parentHeight/4 + "px",
		left: -parentWidth/4 + "px"
	});
	
	$(".bonus-icon").css({
		height: parentHeight/2 + "px",
		width: parentWidth/2 + "px",
		position: "absolute",
		top: parentHeight/4 + "px",
		left: -parentWidth/4 + "px"
	});
};
$(function() {
	showTimeTrack();
});