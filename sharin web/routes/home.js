var models = require("../models");
var fs = require("fs");


module.exports = function(app) {

    app.get("/home", function(req, res, next) {
		fs.readFile("./views/home.html", function(err, data) {
			res.send(data.toString());
		});
	});
}