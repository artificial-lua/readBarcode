// express 모듈
const express = require('express')
const app = express()

// 커스텀 모듈 - config decoder
config = require('./config-decoder').open('./config.json')
port = config['port']

// url에 따라 분리해줍니다.
app.get('/barcode/req/', function(req, res){
	var respond = req.query;
    console.log(respond);
	const ip = req.headers['x-forwarded-for'] ||  req.connection.remoteAddress;
	var date = new Date();
	res.send(respond);
	console.log('[' + date + ']::/barcode/req:' + ip + JSON.stringify(respond));
	db = require('./mariadb-connector').host(config['db'])
	db.query(`SHOW DATABASES`, function(error, result){
		console.log(result)
	})
})

app.get('/condition/check', function(req, res){
	res.send(db = require('./mariadb-connector').host(config['db']))
})

// 마지막으로, 포트로 열어줍니다.
app.listen(port, function(){
	console.log('listening on port ' + port + '!');
	})
