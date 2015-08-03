$(document).ready(function() {
	$("body").css({"background": "#542", "color": "white", "font-family": "Tahoma", "text-align": "center"});

	$("a").css({"text-decoration": "none", "color": "orange"});

	$("ul li").css({"display": "inline", "margin-right": "10px"});

	$("#menu").css({"height": "40px"});

	$("h1").css({"color": "orange"});

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
	
	$("input").css({"color": "white", "background-color": "#754", "border": "2px solid orange"});
	$("input[type=number]").css({"color": "white", "background-color": "#754", "border": "2px solid orange"});
	$("input[type=submit]").css({"background-color": "#321", "border": "2px solid orange", "color": "white"});
	$("input[type=submit]").mouseover(function() {
		$(this).css({"background-color": "#542", "border": "3px solid orange", "color": "white", "margin-left": "-1px", "margin-top": "-1px"});
	});
	$("input[type=submit]").mouseout(function() {
		$(this).css({"background-color": "#321", "border": "2px solid orange", "color": "white", "margin-left": "0px", "margin-top": "0px"});
	});
	
	$("h3").css({"margin-bottom": "3px"});
	
});