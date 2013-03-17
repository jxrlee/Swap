$(document).ready(function() {

	$('#scroll').click(function(){
		$('html, body').animate({
			scrollTop: $("#first").offset().top
			}, 1000);
	});
	

});	

/*function pageScroll() {
	    	window.scrollBy(0,565); // horizontal and vertical scroll increments
	}*/