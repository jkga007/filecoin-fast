$('.first.circle').circleProgress({
    value: 0.80
}).on('circle-animation-progress', function(event, progress) {
    $(this).find('strong').html(parseInt(80 * progress) + '<i>%</i>');
});
$('.second.circle').circleProgress({
    value: 0.65
}).on('circle-animation-progress', function(event, progress) {
    $(this).find('strong').html(parseInt(65 * progress) + '<i>%</i>');
});
$('.third.circle').circleProgress({
    value: 0.50
}).on('circle-animation-progress', function(event, progress) {
    $(this).find('strong').html(parseInt(50 * progress) + '<i>%</i>');
});
$('.fourth.circle').circleProgress({
    value: 0.90
}).on('circle-animation-progress', function(event, progress) {
    $(this).find('strong').html(parseInt(90 * progress) + '<i>%</i>');
});
$('.fifth.circle').circleProgress({
    value: 0.30
}).on('circle-animation-progress', function(event, progress) {
    $(this).find('strong').html(parseInt(30 * progress) + '<i>%</i>');
});
$('.sixth.circle').circleProgress({
    value: 0.60
}).on('circle-animation-progress', function(event, progress) {
    $(this).find('strong').html(parseInt(60 * progress) + '<i>%</i>');
});