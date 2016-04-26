var express = require("express")
var bodyParser = require("body-parser");
var cookieParser = require("cookie-parser");
var app = express();

app.use(express.static('css'));
app.use(express.static('js'));
app.use(express.static('font-awesome'));
app.use(express.static('img'));
app.use(express.static('plugin'));



app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));


app.use(cookieParser());


require("./routes")(app);

  app.get("/", function(req, res, next) {    
		res.redirect('/plugin');
    });

app.listen(8888, function() {
    console.log("Server started port 8888...");
})