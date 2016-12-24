/**
 * 
 */
function showTimeTrack() {
	$(".time-track-square").each(function() {
		$(this).css({
			height: $(this).width() + "px"
		});
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
	$("#time-track-square-0").parent().toggleClass("col-xs-offset-3", true);
	var incomeDiv = $("<div></div>", {
		class: 'income-icon'
	});
	var parentHeight = $(".time-track-income").outerWidth();
	var parentWidth = $(".time-track-income").outerWidth();
	incomeDiv.css({
		height: parentHeight/2 + "px",
		width: parentWidth/2 + "px",
		position: "absolute",
		top: parentHeight/4 + "px",
		left: -parentWidth/4 + "px"
	});
	$(".time-track-income").append(incomeDiv);
	
	var bonusDiv = $("<div></div>", {
		class: 'bonus-icon'
	});
	bonusDiv.css({
		height: parentHeight/2 + "px",
		width: parentWidth/2 + "px",
		position: "absolute",
		top: parentHeight/4 + "px",
		left: -parentWidth/4 + "px"
	});
	$(".time-track-bonus").append(bonusDiv);
};
$(function() {
	showTimeTrack();
});