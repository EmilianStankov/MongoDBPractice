$(document).ready(function() {
	$("li").mouseover(function() {
		$(this).css({"background-color": "orange", "list-style-type": "none"});
		$(this).css({"width": "400px", "height": "40px"});
		$(this).css({"border-radius": "10px", "padding": "5px 10px"});
	});
	
	$("li").mouseout(function() {
		$(this).css({"background-color": "#542", "list-style-type": "disc"});
		$(this).css({"width": "300px", "height": "1.125em"});
		$(this).css({"border-radius": "0px", "padding": "0px"});
	});
	
	$("a").mouseout(function() {
		$(this).css({"color": "orange"});
	});
	
	$("a").mouseover(function() {
		$(this).css({"color": "white"});
	});
	
	$(".menuToggle").on("click", function() {
		  $("ul").toggle("slow");
		  if($(".menuToggle").text() == 'Hide menu'){
	           $(".menuToggle").text('Show menu');
	      } else {
	           $(".menuToggle").text('Hide menu');
	      }
	});
	
	$("#menu").mouseover(function() {
		  console.log("height: " + $(this).height());
		  console.log("width: " + $(this).width());
		  console.log("background: " + $("li").css("background"));
	});
});