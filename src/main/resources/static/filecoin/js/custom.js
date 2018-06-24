$(function() {
"use strict";

function scrollfix () {
	var scroll = $(window).scrollTop();    
	if (scroll >500) {
  		$(".home2 #header").addClass('fix');
	}
	else
	{	
		$(".home2 #header").removeClass('fix');
	}
}
function bannerheight () {
    var bannerheight = $(".banner").height();
	$(".banner-detail").css('top', Math.round(bannerheight/3.5)+'px');

    var full_onescreen_banner_height = $("#container").height();
	$(".banner-detail").css('top', Math.round(full_onescreen_banner_height/3)+'px');
}
function re_size () {
	if ($( window ).width() <= 991) {
		$('.nav li').on('click', function () {
		$('.#menu ul').css({"display":"none"});
		});
		
		/* ------------bannre button margin ------------- */
		$('.cd-intro').children('button').removeClass('mt_30').addClass('mt_20');           
	}
	if ($(window).width() > 991) {
		/* ------------bannre button margin ------------- */
		 $('.cd-intro').children('button').addClass('mt_30').removeClass('mt_20');
	}
}

function owl_carousel () {
/* ------------ OWL Slider Start  ------------- */
	/* ----- client slider Start  ------ */
	$('.client').owlCarousel({
		loop:true,
		autoplay:true,
		responsiveClass:true,
		items : 1, //10 items above 1000px browser width
		responsive:{
			0:{
				items:1,
				nav:false
			},
			600:{
				items:1,
				nav:true
			},
			1000:{
				items:1,
				nav:true,
				loop:true
			}
		}
	})
	/* ----- client slider End  ------ */
	
	/* ----- blog post Start  ------ */
	$('#blog-post').owlCarousel({
		loop:false,
		autoplay:false,
		responsiveClass:true,
		items : 3, //10 items above 1000px browser width
		responsive:{
			0:{
				items:1,
				nav:false
			},
			600:{
				items:2,
				nav:false
			},
			1000:{
				items:3,
				nav:true,
				loop:true
			}
		}
	})
	/* ----- blog post End  ------ */
	
	/* ----- team Start  ------ */
	$('.team3col').owlCarousel({
		autoplay:false,
		responsiveClass:true,
		items : 3, //10 items above 1000px browser width
		responsive:{
			0:{
				items:1,
				nav:false
			},
			600:{
				items:2,
				nav:true
			},
			1000:{
				items:3,
				nav:true,
			}
		}
	
	})

	/* ----- team End  ------ */
/* ------------ OWL Slider End  ------------- */
}

function parallax (){
    var isMobile = /Android|webOS|iPhone|iPad|iPod|BlackBerry/i.test(navigator.userAgent);
    if(!isMobile) {
        if($(".parallax").length){  $(".parallax").sitManParallex({  invert: false });};
    }else{
        $(".parallax").sitManParallex({  invert: true });

    }
}
/* ===== Right Click Disable   =====   */
//document.oncontextmenu = document.body.oncontextmenu = function() {return false;}

    $( window ).on("load",function() {
        bannerheight ();
    });
	$( window ).ready(function() {
		$('[data-toggle="tooltip"]').tooltip();
		$(".loder").fadeOut("slow");
		scrollfix ();
		owl_carousel ();
		bannerheight ();
		re_size();
		parallax ();

	});
	$( window ).resize(function() {
		re_size();
		bannerheight ();
	});

	$(window).scroll(function() {
		scrollfix ();
	});

});
