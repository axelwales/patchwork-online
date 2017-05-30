/**
 * Requires jquery ui
 */
var patchJSON = null;
var globals = new Object();
globals.patchlist = null;
$(function() {
	$.ajax({
        url: $("#json-url").data('json-url'),
        type: 'POST',
        dataType: 'json',
        data: {"jsonRequest":"patches"},
        beforeSend: function() {
        	patchJSON = null;
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("Error: " + textStatus);
            console.log(jqXHR);
        },
        success:function(data){
        	patchJSON = data;
        	globals.patchlist = data;
        	$( "body" ).trigger({
    		  type:"patchesLoaded",
    		  json: globals.patchlist
    		});
        }
    });
});

$( "body" ).on("patchesLoaded", function(event) {
	createPatchList(event.json);
	addPlayerPatch(event.json);
});

function addPlayerPatch(json) {
	var id = $("#current-action-variable").data('patch-id');
	if(id != -1) {
		var patchElements = $('.player-board-patch');
		var isCurrentPlayer = $("#current-player-boolean").data("current-player");
		var hasVisiblePatch = patchElements.length != 0 && patchElements.width() != 0;
		if(isCurrentPlayer == true && !hasVisiblePatch) {
			var patch = null;
			if(globals.boardPatch != null && patchElements.length != 0 && patchElements.width() == 0) {
				patch = globals.boardPatch;
				patch.remove();
			} else {
				patch = new tile(json["p" + id].map, "p" + id);
				globals.boardPatch = patch;
			}
	    	var playerBoards = $(".player-board-container");
	    	var widths = playerBoards.map(function(i, board) {
	            return $(board).outerWidth();
	    	});
	    	var dimension = Math.max.apply(Math, widths)/9;
	    	playerBoards.each(function() {
	    		if( $(this).data('current-player') == true  ) {
		    		patch.display($(this), 'player-board-patch',dimension,dimension,true);
		    	}
	    	});
		}
	}
}

function createPatchList(json) {
	$('.patch-list-item').each(function() {
		var position = $(this).data("position");
		var numberDiv = $("<span></span>", {
			class: "small patch-list-position",
		});
		numberDiv.text(position);
		$(this).append(numberDiv);
		
		var id = $(this).attr('id');
		var patch = new tile(json[id].map,id);
		var patchContainer = $('<div></div>', {
			class: 'patch-list-patch-container',
			width: $(this).width()/2 + "px"
		});
		$(this).append(patchContainer);
		var defaultWidth = $(this).width()/8;
		var computedWidth = $(this).width()/(2*(patch.maxX() - patch.minX() + 1));
		var width = Math.min(defaultWidth, computedWidth);
		patch.display(patchContainer, 'patch-list-patch', width, width, false);
		
		var statsContainer = $('<div></div>', {
			class: 'patch-list-stats-container pull-right',
			width: $(this).width()/2 + "px"
		});
		var costP = $("<p></p>", {
			class: "patch-list-stat",
		});
		costP.text('cost: ' + json[id].cost);
		statsContainer.append(costP);
		var timeP = $("<p></p>", {
			class: "patch-list-stat",
		});
		timeP.text('time: ' + json[id].time);
		statsContainer.append(timeP);
		var incomeP = $("<p></p>", {
			class: "patch-list-stat",
		});
		incomeP.text('income: ' + json[id].income);
		statsContainer.append(incomeP);
		$(this).append(statsContainer);

	});
};

function tile(map, name) {
  	this.startMap = map,
  	this.previousMap = map,
    this.currentMap = map,
    this.name = name,
    this.classString = null,
    this.height = 0,
    this.width = 0,
    this.parent = null,
    this.x = 0,
    this.y = 0,
    this.rotations = 0,
    this.flipped = 0,
    this.display = function(parent, classString, blockHeight, blockWidth, setDraggable) {
  		this.parent = parent;
  		this.height = blockHeight;
  		this.width = blockWidth;
  		this.classString = classString;
  		for (var i = 0; i < this.startMap.length; i++) {
  			var divId = this.name + "-" + i;
  			if(setDraggable == true)
  				divId += "-pb";
	        var div = $("<div></div>", {
	        'data-x-offset': this.startMap[i][0],
	        'data-y-offset': this.startMap[i][1],
	        class: classString,
	        id: divId
	        });
	        if( i == 0) {
	        	this.x = this.startMap[i][0] - this.minX();
		        this.y = 8 - (this.startMap[i][1] - this.minY());
		        this.flipped = 0;
		        this.rotations = 0;
	        }
	        div.css({
	        	width: blockWidth + "px",
	            height: blockHeight + "px",
	            left: (this.startMap[i][0] - this.minX()) * blockWidth + "px",
	            top: (this.maxY() - this.startMap[i][1]) * blockHeight + "px"
	        });
	        div.appendTo(parent);
	        if(setDraggable == true) {
	        	this.createContainmentBox(div, this.startMap[i][0], this.startMap[i][1], setDraggable);
	        }
	    }
    },
    this.createContainmentBox = function(target, x, y, setDraggable) {
    	var boxLeft = x - this.minX();
		var boxWidth = ((9 - (this.maxX() - x)) - boxLeft)*this.width;
		var boxTop = this.maxY() - y;
		var boxHeight = ((9 - (y - this.minY())) - boxTop)*this.height;
		var cssId = x + '-' + y + '-containment-box';
		var box = $("<div></div>", {
			id: cssId,
			class: 'patch-containment-box'
		});
		box.css({
			position: 'absolute',
			height: boxHeight + 'px',
			width: boxWidth + 'px',
			top: boxTop*this.height+ 'px',
			left: boxLeft*this.width + 'px'
		});
		box.css("z-index", -1);
		this.parent.append(box);
		if(setDraggable == true) 
			this.setTileDraggable(target, '#' + cssId);
		else
			target.draggable("option", "containment", '#' + cssId);
    },
    this.setTileDraggable = function(target, containmentSelector) {
    	var tile = this;
    	target.draggable({
    		snapTolerance: tile.width*.98,
    		create: function(event, ui) {
    			$(this).draggable("option", "containment", containmentSelector);
    		},
    		drag: function(event, ui) {
    			var snapTolerance = $(this).draggable('option', 'snapTolerance');
    			var topRemainder = ui.position.top % tile.height;
    			var leftRemainder = ui.position.left % tile.width;
    			
    			if (topRemainder <= snapTolerance) {
    				ui.position.top = ui.position.top - topRemainder;
    			}
    			
    			if (leftRemainder <= snapTolerance) {
    				ui.position.left = ui.position.left - leftRemainder;
    			}
    			$('.'+ tile.classString).each(function() {
    				var xOffset = tile.getX($(this)) - tile.getX(ui.helper);
    				var yOffset = tile.getY($(this)) - tile.getY(ui.helper);
    				$(this).css({
    				    'top': ui.position.top - yOffset * tile.height + 'px',
    				    'left': ui.position.left + xOffset * tile.width + 'px'
    				});
    			});
    		},
    		stop: function(e, ui) {
    			var root = $('#' + tile.name + '-0-pb');
    			tile.x = Math.round(root.position().left/tile.width);
    			tile.y = 8 - Math.round(root.position().top/tile.height);
    		}
    	});
    },
    this.rotate = function() {
    	this.previousMap = this.currentMap;
    	this.currentMap = $.map(this.currentMap, function(point) {
    		return [
    		    [point[1], -point[0]]
    		];
    	});
    	this.rotations = (this.rotations + 1) % 4;
    	this.updateDisplay();
    },
    this.flip = function() {
    	var rotations = this.rotations;
    	this.previousMap = this.currentMap;
    	this.currentMap = $.map(this.currentMap, function(point) {
    		if(rotations % 2 == 1) {
    			return [
	    		    [-point[0], point[1]]
	    		];
    		} else {
	    		return [
	    		    [point[0], -point[1]]
	    		];
    		}
    	});
    	this.flipped = (this.flipped + 1) % 2;
    	this.updateDisplay();
    },
    this.updateDisplay = function() {
	    var root = $('#' + this.name + '-0-pb');
	    var left = parseInt(root.css('left'), 10);
	    var top = parseInt(root.css('top'), 10);
	    for (var i = 0; i < this.currentMap.length; i++) {
		    var div = $("#" + this.name + "-" + i + '-pb');
		    div.data('x-offset', this.currentMap[i][0]);
		    div.data('y-offset', this.currentMap[i][1]);
		    div.css('left', (left + this.currentMap[i][0] * this.width));
		    div.css('top', (top - this.currentMap[i][1] * this.height));
		    if( i == 0) {
		    	this.x = Math.round(div.position().left/this.width);
		    	this.y = 8 - Math.round(div.position().top/this.height);
		    	console.log('(' + this.x + ',' + this.y + ')');
	    	}
		    this.updateContainmentBox(div, this.currentMap[i][0], this.currentMap[i][1], this.previousMap[i][0], this.previousMap[i][1]);
	    }
    },
    this.updateContainmentBox = function(target, newX, newY, oldX, oldY) {
    	this.createContainmentBox(target, newX, newY, false);
    	$('#' + oldX + '-' + oldY + '-containment-box').remove();
    },
    this.remove = function() {
    	$('.' + this.classString).remove();
    	$('.patch-containment-box').remove();
    }
    this.minX = function() {
      var xOffsets = $.map(this.currentMap, function(point) {
        return point[0];
      });
      return Math.min.apply(Math, xOffsets);
    },
    this.maxX = function() {
      var xOffsets = $.map(this.currentMap, function(point) {
        return point[0];
      });
      return Math.max.apply(Math, xOffsets);
    },
    this.minY = function() {
      var yOffsets = $.map(this.currentMap, function(point) {
        return point[1];
      });
      return Math.min.apply(Math, yOffsets);
    },
    this.maxY = function() {
      var yOffsets = $.map(this.currentMap, function(point) {
        return point[1];
      });
      return Math.max.apply(Math, yOffsets);
    },
    this.getX = function(t) {
    	  return parseInt(t.data('x-offset'), 10);
    },
    this.getY = function(t) {
    	  return parseInt(t.data('y-offset'), 10);
    };
} //END tile constructor