// express 모듈
const express = require('express')
const app = express()

// 커스텀 모듈 - config decoder
config = require('./config-decoder').open('./config.json')
port = config['port']
mysql = require('./mariadb-connector');

// url에 따라 분리해줍니다.
app.get('/barcode/req/', function(req, res){
	var respond = req.query;
    console.log(respond);
	const ip = req.headers['x-forwarded-for'] ||  req.connection.remoteAddress;
	var date = new Date();
	res.send(respond);
	console.log('[' + date + ']::/barcode/req:' + ip + JSON.stringify(respond));
	db = mysql.host(config['db'])
	db.query(`SHOW DATABASES`, function(error, result){
		console.log(result)
	})
})

app.get('/condition/check', function(req, res){
	if (req.query['passwd'] == config['admin']['password']){
		var respond = mysql.condition(config['db']);
		res.send("OK");
		console.log(respond);
	}
	else{
		console.log("Wrong Password")
		console.log(req.query['passwd'])
		res.send(req.query['passwd']);
	}
})

// 마지막으로, 포트로 열어줍니다.
app.listen(port, function(){
	console.log('listening on port ' + port + '!');
	})
