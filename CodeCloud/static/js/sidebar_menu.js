
$("#menu-toggle").click(function(e) {
        e.preventDefault();
        $("#wrapper").toggleClass("toggled");
    });
$("#menu-toggle-2").click(function(e) {
        e.preventDefault();
        $("#wrapper").toggleClass("toggled-2");
        $('#menu ul').hide();
    });

/**
function initMenu() {
      $('#menu ul').hide();
      $('#menu ul').children('.current').parent().show();
      //$('#menu ul:first').show();
      $('#menu li a').click(
        function() {
          var checkElement = $(this).next();
          if((checkElement.is('ul')) && (checkElement.is(':visible'))) {
            return false;
            }
          if((checkElement.is('ul')) && (!checkElement.is(':visible'))) {
            $('#menu ul:visible').slideUp('normal');
            checkElement.slideDown('normal');
            return false;
            }
          }
        );
      }
      $(document).ready(function() {initMenu();});
**/

/**
//so: closing-open-submenu-jquery-accordion
 function initMenu() {
     $('#nav ul').hide();
     $('#nav li a').click(

     function () {
        
         var checkElement = $(this).next();
         if ((checkElement.is('ul')) && (checkElement.is(':visible'))) {
             $('#nav ul:visible').slideToggle('normal');
         }
         if ((checkElement.is('ul')) && (!checkElement.is(':visible'))) {
             removeActiveClassFromAll();
             $(this).addClass("active");
             $('#nav ul:visible').slideToggle('normal');
             checkElement.slideToggle('normal');
             return false;
         }
         
         if($(this).siblings('ul').length==0 && $(this).parent().parent().attr('id')=='nav')
         {
             
             removeActiveClassFromAll();
             $(this).addClass("active");
             $('#nav ul:visible').slideToggle('normal');
             
             return false;
         }
     });
 }
 function removeActiveClassFromAll() {
     $('#nav li a').each(function (index) {
         $(this).removeClass("active");
     });
 }
 $(document).ready(function () {
     initMenu();
 });
 $('#nav').click(function (e) {
     e.stopPropagation();
 })
 $(document).click(function () {
     $('#nav').children('li').each(function () {
         if ($(this).children('ul').css('display') == 'block') {
             $(this).children('ul').slideToggle('normal')
             $(this).children('a').removeClass('active')
         }
     })
 })**/


 /**
//so: clicking-a-link-display-it-in-different-div-on-same-page
$("#nav li a").click(function(e){ //show-hide page contents
    e.preventDefault();
    $(".toggle").hide();
    var toShow = $(this).attr('href');
    $(toShow).show();
});**/
