$.validator.addMethod(
	"date",
    function(value, element, regexp) {
        var check = false;
        return this.optional(element) || regexp.test(value);
    },
    "Please enter a valid date! (From 1900 to 2099)"
);

$("input[type=submit]").focus(function(){
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
				date: /^(0[1-9]|[12][0-9]|3[01])[\.](0[1-9]|1[0-2])[\.](19[0-9][0-9]|20[0-9][0-9])/
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
