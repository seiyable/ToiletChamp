//server for Toilet Champ

//======== require libraries ========
var express = require('express');
var expressHbs = require('express3-handlebars');
var bodyParser = require('body-parser');
var cookieParser = require('cookie-parser');
var expressSession = require('express-session');
var MongoClient = require('mongodb').MongoClient;
var mongoUrl = "mongodb://seiyable:Ketta5884@dbh29.mongolab.com:27297/toilet_champ";
//var mongoose = require('mongoose');
var cloudinary = require('cloudinary');
var multipart = require('connect-multiparty');
var multipartMiddleware = multipart();

//require data from local directory
//var person = require('./defaultValues');

var port = Number(process.env.PORT || 5000);
console.log('Listening on port',port);

var db; //shoud exist before connected to DB

//======== handlebars helper functions ========
var handlebars = expressHbs.create({

//    helpers: {
//        foo: function () { return 'FOO!'; }
//        bar: function () { return 'BAR!'; }
//    }
});


//======== initial settings ========
var app = express();

app.engine('handlebars', handlebars.engine);
app.set('view engine', 'handlebars');

app.use(bodyParser());
// must use cookieParser before expressSession
app.use(cookieParser());
app.use(expressSession({secret:'somesecrettokenhere'}));

//database for images
cloudinary.config({ 
	cloud_name: 'dpcrks7mn', 
	api_key: '286594891765139', 
	api_secret: 'P37PP4ENa9XjtA2S484g5ZFMzv0' 
});



//======== mongoose schemas ========
/*
var Schema = mongoose.Schema;

var UserSchema = new Schema({
	udid: String,
	username: String,
	usericonURL: String
});
mongoose.model('User', UserSchema);

var ActivitySchema = new Schema({
	udid: String,
	location: String,
	timestamp: Date
});
mongoose.model('Activity', UserSchema);


mongoose.connect('mongodb://seiyable:Ketta5884@dbh29.mongolab.com:27297/toilet_champ');
*/
//======== middleware functions ========
//app.use(function(req, res, next){
//  console.log('%s %s', req.method, req.url);
//  next();
//});



//======== response functions to HTTP accsess ========
app.get('/', function(req, res){
	//--- initialize variables for response ---
	var username = "not entered";
	var password = "not entered";

	//--- set data into the data object for response ---
	var data = {};
	console.log("username: " + username);
	console.log("password: " + password);
	data.username = username;
	data.password = password;

	res.render('layouts/login', data);
});

app.post('/', multipartMiddleware, function(req, res){

	//--- initialize variables for response ---
	var username = "not entered";
	var password = "not entered";
	var file = req.body.file;

	//--- set data into the data object for response ---
	var data = {};
	console.log("username: " + username);
	console.log("password: " + password);
	data.username = username;
	data.password = password;

    cloudinary.uploader.upload(
    	req.files.file.path,
    	function(result) {
  			console.log(result);
  			var imageUrl = result.url;
  			//store it to database
    	},
    	{
      		eager: [
        		{ width: 200, height: 200, crop: 'thumb', gravity: 'face',
          		radius: 20 /*,effect: 'sepia'*/ }
        		//{ width: 100, height: 150, crop: 'fit', format: 'png' }
      		]
    	}
    )

	//--- take values from request ---
	if (req.body.username){
		username = req.body.username;
	}
	if (req.body.username){
		password = req.body.password;
	}

    /*
	//--- authenticate log-in ---
	if (auth_login) {
		auth_result = true;
	}else{
		auth_result = false;
	}
	*/
	

	res.render('layouts/login', data);
});


//======== HTTP request for user table ========
//------ add a user ------
// url: /add_user?udid=ALJFHASDFH&username=Seiya&usericon=1100
app.post('/add_user', multipartMiddleware, function(req, res){

	var udid = req.files.udid;
	var username = req.files.username;
	var usericonURL = req.files.usericonURL;

	var collection = db.collection('usertable');
	collection.insert( {udid : udid, username: username, usericonURL: usericonURL}, function(err, count){
    if(err){
    	console.log('There was an error' + err);
    }
    res.send('The count is: ' + count);
	});

/*
	var User = mongoose.model('User');
	var user = new User();
	user.udid = req.files.udid;
	user.username = req.files.username;
	user.usericonURL = req.files.usericonURL;
	user.save(function(err) {
  		if (err) {
  			console.log(err);
  		}
	});
*/
});

//------ get a user ------
// url: /get_user?udid=ALJFHASDFH
app.post('/get_user', function(req, res){

	var udid = req.body.udid;
	var collection = db.collection('usertable');
	collection.find({udid:udid}).toArray(function(err, items){
  		if(err){
  			console.log('There was an error' + err);
  		}
		if (items.length > 0){
    		var data = {};
    		data.udid = udid;
    		data.username = items[0].username;
    		res.render('layouts/user_list', data);
		} else {
			res.send("Did not find any data");
		}
	});

/*
	var udid = req.body.udid;
	var User = mongoose.model('User');
	User.find({udid:udid}).toArray(function(err, items){
  		if(err){
  			console.log('There was an error' + err);
  		}
		if (items.length > 0){
    		var data = {};
    		data.udid = udid;
    		data.username = items[0].username;
    		res.render('layouts/user_list', data);
		} else {
			res.send("Did not find any data");
		}
	});
*/	
});


//======== HTTP request for activity table ========
//------ add an activity ------
// url: /add_activity?udid=ALJFHASDFH&location=shop&timestamp=1234
app.post('/add_activity', function(req, res){

	var udid = req.body.udid;
	var location = req.body.location;
	var timestamp = req.body.timestamp;

	var collection = db.collection('activitytable');
	collection.insert( {udid : udid, location: location, timestamp: timestamp}, function(err, count){
    	if(err){
    		console.log('There was an error' + err);
    	}
    	res.send('The count is: ' + count);
	});

/*
	var Activity = mongoose.model('Activity');
	var activity = new Activity();
	activity.udid = req.body.udid;
	activity.location = req.body.location;
	activity.timestamp = req.body.timestamp;
	activity.save(function(err) {
  		if (err) {
  			console.log(err);
  		}
	});
*/
});

//------ get an activity ------
// url: /get_activity?udid=ALJFHASDFH
app.post('/get_activity', function(req, res){

	var udid = req.body.udid;
	//var timeFilter = req.body.timeFilter;
	//var locationFilter = req.body.locationFilter;
	//var itemNumber = req.body.itemNumber;

	var collection = db.collection('activitytable');
	collection.find({udid:udid}).toArray(function(err, items){
  		if(err){
  			console.log('There was an error' + err);
  		}
		if (items.length > 0){
    		var data = {};
    		data.items = items;
    		//res.render('layouts/activity_list', data);
    		res.send(data);
		} else {
			res.send("Did not find any data");
		}
	});

/*
	var udid = req.body.udid;
	//var timeFilter = req.body.timeFilter;
	//var locationFilter = req.body.locationFilter;
	//var itemNumber = req.body.itemNumber;
	var Activity = mongoose.model('Activity');
	Activity.find({udid:udid},function(err, items){
  		if(err){
  			console.log('There was an error' + err);
  		}
		if (items.length > 0){
    		var data = {};
    		data.items = items;
    		//res.render('layouts/activity_list', data);
    		res.send(data);
		} else {
			res.send("Did not find any data");
		}
	});
*/
});



//======== assign port number ========
MongoClient.connect(mongoUrl, function(_err, _db){
    if(_err){
    	console.log('There was an error..' + err);
    }
    db = _db;
	console.log('connected to mongo!!');
	app.listen(port);
})
//app.listen(port);