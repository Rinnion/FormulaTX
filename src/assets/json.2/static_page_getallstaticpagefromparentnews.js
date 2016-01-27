var http = require('http');
var fs = require('fs');

var id = process.argv[2];

var filename = id + "-news.json";
var url = "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentnews&id=" + id;

var file = fs.createWriteStream(filename);
var request = http.get(url, function(response) {
  response.pipe(file);
});
