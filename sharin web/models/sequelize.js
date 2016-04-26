var Sequelize = require("sequelize");

module.exports = new Sequelize("sharin_web", "root", "", {
    pool : false,
    host : "localhost",
    port : 3306
});