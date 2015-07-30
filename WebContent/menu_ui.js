$(document).ready(function() {
	$("li").mouseover(function() {
		$(this).css({"background-color": "#ef8", "font-size": "200%", "list-style-type": "none"});
		$(this).css({"width": "300px", "height": "30px"});
	});
	
	$("li").mouseout(function() {
		$(this).css({"background-color": "#fff", "font-size": "100%", "list-style-type": "disc"});
		$(this).css({"width": "300px", "height": "1.125em"});
	});
	
	$("h2").on("click", function() {
		  $("ul").toggle("slow");
	});
});