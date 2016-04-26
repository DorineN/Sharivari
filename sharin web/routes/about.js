var models = require("../models");
var fs = require("fs");


module.exports = function(app) {

    app.get("/download", function(req, res, next) {
		fs.readFile("./views/download.html", function(err, data) {
			res.send(data.toString());
		});
	});
}