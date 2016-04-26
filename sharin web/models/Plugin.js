var sequelize = require("./sequelize");

module.exports = sequelize.import("plugin", function(sequelize, Datatypes) {
    return sequelize.define("Plugin", {
        idPlugin: {
            type: Datatypes.BIGINT,
            primaryKey: true,
            autoIncrement: true
        },
		 name: {
            type: Datatypes.STRING
        },
        description: {
            type: Datatypes.TEXT
        },
        url: {
            type: Datatypes.STRING
        }
    }, {
            paranoid: true,
            freezeTableName: true,
            underscored: true
        });
});