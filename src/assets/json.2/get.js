var http = require('http');
var fs = require('fs');

var filename = process.argv[2];
var url = process.argv[3];


var file = fs.createWriteStream(filename);
var request = http.get(url, function(response) {
  response.pipe(file);
});
