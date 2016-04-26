module.exports = function(app) {
    
    require("./plugin")(app);
	require("./addPlugin")(app);
	require("./connection")(app);
	require("./home")(app);
	require("./download")(app);
	require("./about")(app);
    
}