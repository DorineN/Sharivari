var sequelize = require("./sequelize");

module.exports = sequelize.import("user", function(sequelize, Datatypes) {
    return sequelize.define("User", {
        idUser: {
            type: Datatypes.BIGINT,
            primaryKey: true,
            autoIncrement: true
        },
        login: {
            type: Datatypes.STRING
        },
        pwd: {
            type: Datatypes.STRING
        }
    }, {
            paranoid: true,
            freezeTableName: true,
            underscored: true
        });
});