$(document).ready(function() {
	$("li").mouseover(function() {
		$(this).css({"background-color": "orange", "list-style-type": "none"});
		$(this).css({"width": "400px", "height": "40px"});
		$(this).css({"border-radius": "10px", "padding": "5px 10px"});
		$(this).css({"font-weight": "bold"});
	});
	
	$("li").mouseout(function() {
		$(this).css({"background-color": "#542", "list-style-type": "disc"});
		$(this).css({"width": "300px", "height": "1.125em"});
		$(this).css({"border-radius": "0px", "padding": "0px"});
		$(this).css({"font-weight": "normal"});
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
	
	$("input[type=text]").css({"background-color": "#754", "border": "2px solid orange"});
	$("input[type=number]").css({"background-color": "#754", "border": "2px solid orange"});
	$("input[type=submit]").css({"background-color": "#321", "border": "2px solid orange", "color": "white"});
	$("input[type=submit]").mouseover(function() {
		$(this).css({"background-color": "#542", "border": "3px solid orange", "color": "white", "margin-left": "-1px", "margin-top": "-1px"});
	});
	$("input[type=submit]").mouseout(function() {
		$(this).css({"background-color": "#321", "border": "2px solid orange", "color": "white", "margin-left": "0px", "margin-top": "0px"});
	});
});