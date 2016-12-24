/**
 * 
 */
/*$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
  e.target // newly activated tab
  e.relatedTarget // previous active tab
})*/


$(function() {
	
	view.viewSize = new Size($('.tab-content').width(), $('.tab-content').width());
	view.draw();
	
	//buildTree(treeData);

});

function renderNextNode(tree) {
	
}

function linkedTree(list) {
	this.root = list[0],
	this.current = list[0],
	this.list = list,
	this.index = 0
}

function linkedTreeNode(height, parent, children, childIndex, iterations) {
	this.height = height,
	this.offset = parent.offset * (childIndex/parent.children.length),
	this.active = true,
	this.defaultPolicyIterations = iterations,
	this.parent = parent,
	this.children = children
}

function buildTree(treeData) {
	//var treeContainer = $('#tree-container');
	buildTreeRecursive(treeData.root);
}

function buildTreeRecursive(node) {
	while (node != null) {
		buildNode(node);
		if(node.children != null) {
			$.each(node.children, function() {
				buildTreeRecursive(this);
			});
		}
	}
}

function buildNode(nodeData) {
	var nodeSize = 20;
	var strokeWidth = 10;
	var levelIncrement = 50;
	
	var path = new Path();
	path.strokeColor = 'black';
	path.strokeWidth = strokeWidth;
	path.strokeCap = 'round';
	
	var start = new Point(view.center.x, 100);
	// Move to start and draw a line from there
	path.moveTo(start);
	// Note the plus operator on Point objects.
	// PaperScript does that for us, and much more!
	path.lineTo(start + [ view.center.x/2, levelIncrement ]);
	
	var circleA = new Path.Circle(start, nodeSize);
	circleA.fillColor = 'black';
	
	var circleB = new Path.Circle(start + [ view.center.x/2, levelIncrement ], nodeSize);
	circleB.fillColor = 'black';
}