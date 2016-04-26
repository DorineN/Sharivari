var models = require("../models");
var fs = require("fs");


module.exports = function(app) {

    app.get("/about", function(req, res, next) {
		fs.readFile("./views/about.html", function(err, data) {
			res.send(data.toString());
		});
	});
}