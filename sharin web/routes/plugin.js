var models = require("../models");
var fs = require("fs");


module.exports = function(app) {

    app.get("/plugin", function(req, res, next) {
		var str="";
		
        var Plugin = models.Plugin;
        var request = {
			  attributes: ['name', 'description', 'url'],
			  order: ['name']
		};
        Plugin.findAll(request).then(function(results) {
			for(var i=0; i<results.length; i++){
				
				str += "<tr>";
				str += "<td>"+ results[i].name +"</td>";	
				str += "<td>"+ results[i].description +"</td>";	
				str += "<td><a href =\""+ results[i].url +"\">Télecharger</a></td>";		
				str += "<tr>";
			}
			if(results.length==0){
				str += "<tr><td colspan=\"3\">No plugin found</td></tr>";	
			}
			
        }).then( function(){
			fs.readFile("./views/plugin.html", function(err, data) {
				res.send(data.toString().split("$results").join(str));
			});
		}).catch(function(err) {
			str += "<tr><td colspan=\"3\">Error SQL plugin</td></tr>";
        });
		
		
		
    });


}