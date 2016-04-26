var models = require("../models");
var fs = require("fs");


module.exports = function(app) {

    app.post("/connection", function(req, res, next) {
		var msg="";
		if(req.body.login && req.body.pwd){
			//check if exist in user
			var User = models.User;
			var request = {
				attributes: ['login'],
				where: {
					login : req.body.login,
					pwd : req.body.pwd
				}
			};
			User.findAll(request).then(function(results) {
				if(results.length!=1){
					msg="Identifiant ou mot de passe incorrecte.";
					fs.readFile("./views/connection.html", function(err, data) {
						res.send(data.toString().split("$msg").join(msg));
					});
				}else{
				
					res.cookie("session", req.body.login, { maxAge: 500000 });
					res.redirect('/addPlugin');
				}
				
			}).catch(function(err) {
				msg="Critical error Connection !!!";
				fs.readFile("./views/connection.html", function(err, data) {
					res.send(data.toString().split("$msg").join(msg));
				});
					
			});
			
		}else{
			msg="Vous devez entrer un identifiant et un mot de passe !";
			fs.readFile("./views/connection.html", function(err, data) {
				res.send(data.toString().split("$msg").join(msg));
			});
		
		}

		
    });




    app.get("/connection", function(req, res, next) {
		
		var msg ="";
	
		if(req.cookies.session){
			res.redirect('/addPlugin');
		}else{
			fs.readFile("./views/connection.html", function(err, data) {
			   res.send(data.toString().split("$msg").join(msg));
			});
		}
		
    });
	
	
	
}