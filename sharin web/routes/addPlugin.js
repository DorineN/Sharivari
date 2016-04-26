var models = require("../models");
var fs = require("fs");


module.exports = function(app) {

	app.get("/addPlugin", function(req, res, next) {
		var msg="";
		if(req.cookies.session){
			
			fs.readFile("./views/addPlugin.html", function(err, data) {
				res.send(data.toString().split("$msg").join(msg));
			});
		}else{
			msg="Vous devez être connecté pour accéder à cette page.";
			fs.readFile("./views/connection.html", function(err, data) {
				res.send(data.toString().split("$msg").join(msg));
			});
		}
	});
	

	
	 app.post("/addPlugin", function(req, res, next) {
		var msg="";
		if(req.cookies.session){
			 console.log(files.myFile.filename);			
			if(req.body.name && req.body.description){
				var Plugin = models.Plugin;
				Plugin.create({
					name: req.body.name, 
					description: req.body.name,
					url : "vide"
				})
				fs.readFile("./views/addPlugin.html", function(err, data) {
					res.send(data.toString().split("$msg").join(msg));
				});
				msg="Plugin ajouté !";
			}else{
				msg="Tous les champs doivent être rempli.";
				fs.readFile("./views/addPlugin.html", function(err, data) {
					res.send(data.toString().split("$msg").join(msg));
				});
			}	
		}else{
			msg="Vous devez être connecté pour accéder à cette page";
			fs.readFile("./views/connection.html", function(err, data) {
				res.send(data.toString().split("$msg").join(msg));
			});
		}
		
    });


}