var sequelize = require("./sequelize");
var User = require("./User");
var Plugin = require("./Plugin");

sequelize.sync();

module.exports = {
    "sequelize" : sequelize,
    "User" : User,
	"Plugin" : Plugin
};
