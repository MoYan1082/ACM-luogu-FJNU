var page = require('webpage').create();

page.open('http://localhost:8080/1', function() {
  setTimeout(function() {
      page.render('./resources/ranklist.png');
      phantom.exit();
  }, 200);
});