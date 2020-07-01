// $(window).load(function() {
//     $('.flexslider').flexslider({
//         animation: "slide",
//         controlNav: "thumbnails"
//     });
// });

$(function() {
    SyntaxHighlighter.all();
});
$(window).load(function() {
    $('.flexslider').flexslider({
        animation: "slide",
        start: function(slider) {
            $('body').removeClass('loading');
        }
    });
});