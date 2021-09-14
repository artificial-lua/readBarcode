// express 모듈
const express = require('express')
const app = express()

// 커스텀 모듈 - config decoder
config = require('./config-decoder').open('./config.json')
db = config['db'];
port = config['port']
mysql = require('./mariadb-connector');
debug = config['debug']

function log(log){
	if (debug == true){
		console.log(log)
	}
}

// url에 따라 분리해줍니다.
// app.get('/barcode/req/', function(req, res){
// 	var respond = mysql.host(db);
//     console.log(respond);
// 	const ip = req.headers['x-forwarded-for'] ||  req.connection.remoteAddress;
// 	var date = new Date();
// 	res.send(respond);
// 	console.log('[' + date + ']::/barcode/req:' + ip + JSON.stringify(respond));
// })
app.get('/barcode/reg/', function(req, res){
	var body = req.query
	
	var result = mysql.barcode_reg(db, body)

	log(req.query,toString + result)
	res.send(result)
})

app.get('/user/reg/', function(req, res) {
	var result = mysql.user_reg(db);

	var result = {
		error : false,
		result : result
	}
	log(result)
	res.send(result)
})

app.get('/condition/check', function(req, res){
	if (req.query['passwd'] == config['admin']['password']){
		var result = mysql.condition(db);
		res.send("ok");
	}
	else{
		log("Wrong Password")
		res.send(req.query['passwd']);
	}
})

// 마지막으로, 포트로 열어줍니다.
app.listen(port, function(){
	log('listening on port ' + port + '!');
	})
