$("input[type=submit]").click(function(){
	$("form").validate({
		errorClass: "err",
		rules: {
			name: {
				required: true,
				minlength: 3
			},
			description: {
				required: true,
				minlength: 10
			},
			date: {
				required: true,
			},
			actorName: {
				required: true,
				minlength: 3
			},
			movieName: {
				required: true,
				minlength: 1
			},
			year: {
				required: true,
				number: true,
				min: {
					param: 1900
				}
			}
		}
	});
	
	if($("form").valid()) {
		$("#message").text("success");
	} else {
		$(".err").css({"margin-left": "10px", "color": "#f00", "font-weight": "bold", "font-size": "0.7em", "font-style": "italic"});
		$("input").css({"color": "white", "background-color": "#754", "border": "2px solid orange", "font-size": "10pt"});
		$("#message").text("invalid input");
	}
});
