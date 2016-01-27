var http = require('http');
var fs = require('fs');

var suffix = process.argv[2];
var method = process.argv[3];
var id = process.argv[4];

var filename = id + "-" +suffix + ".json";
var url = "http://app.formulatx.com/api/static_page?method=" + method + "&id=" + id;

var file = fs.createWriteStream(filename);
var request = http.get(url, function(response) {
  response.pipe(file);
});
