var page = require('webpage').create();
var url = 'http://localhost:$port/greeting.html';

page.open(url, function(status) {
    page.includeJs("http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js", function() {
        var elements = page.evaluate(function() {
            console.log('evaluate');
            return $$('span').length;
        });
      console.log(elements);

      phantom.exit();
    });
});
