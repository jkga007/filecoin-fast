(function($) {

  $.fn.menumaker = function(options) {
      
      var cssmenu = $(this), settings = $.extend({
        format: "dropdown",
        sticky: false
      }, options);

      return this.each(function() {
          cssmenu.prepend('<div id="menu-button"><a class="mobile-menu-icon bar-icon"><i class="fa fa-bars"></i></a></div>');
        $(this).find("#menu-button,.nav li").on('click', function(){
          $(this).toggleClass('menu-opened');
          var mainmenu = $(this).next('ul');
          if (mainmenu.hasClass('open')) { 
            mainmenu.hide().removeClass('open');
			mainmenu.parent().siblings().children().removeClass('open');
          }
          else {
            mainmenu.show().addClass('open');
            if (settings.format === "dropdown") {
              mainmenu.find('ul').show();
            }
          }
        });
		
		 $(this).find(".nav li").on('click', function(){
          $('#menu-button').toggleClass('menu-opened');
		  var mainmenu = $('#menu-button').next('ul');
		  if (mainmenu.hasClass('open')) { 
            mainmenu.hide().removeClass('open');
			mainmenu.parent().siblings().children().removeClass('open');
          }
        });

        cssmenu.find('li ul').parent().addClass('nav-has-sub');

        multiTg = function() {
          cssmenu.find(".nav-has-sub").prepend('<span class="submenu-button"></span>');
          cssmenu.find('.submenu-button').on('click', function() {
            $(this).toggleClass('submenu-opened');
            if ($(this).siblings('ul').hasClass('open')) {
/*                $(this).siblings('ul').removeClass('open').hide();
*/				            
					$(this).parent().children('ul').removeClass('open').hide();
/*			$(this).parent().siblings().children('span').removeClass('submenu-opened'); 	  
*/            }
            else {
                $(this).siblings('ul').addClass('open').show();
            }
          });
        };

        if (settings.format === 'multitoggle') multiTg();
        else cssmenu.addClass('dropdown');

        if (settings.sticky === true) cssmenu.css('position', 'fixed');

        resizeFix = function() {
          if ($( window ).width() > 991) {
              cssmenu.find('ul').show();
              
          }
          if ($(window).width() <= 991) {
            cssmenu.find('ul').hide().removeClass('open');
          }
		  
        };
        resizeFix();
        return $(window).on('resize', resizeFix);

      });
  };
})(jQuery);

(function ($) {
    $(document).ready(function () {

        $("#menu").menumaker({
            format: "multitoggle"
        });
        
        $("li.nav-has-sub").on("mouseenter", function(){

            $(this).find("ul").removeClass("showleft");
            if ($(this).find("ul")[0].offsetWidth + $(this).find("ul").offset().left > document.body.offsetWidth)
            {
                $(this).find("ul").addClass("showleft");
            }

        });

    });
})(jQuery);

