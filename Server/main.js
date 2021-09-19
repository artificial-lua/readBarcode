// express 모듈
const express = require('express')
const app = express()

// 커스텀 모듈 - config decoder
config = require('./config-decoder').open('./config.json')
db = config['db'];
port = config['port']
mysql = require('./mariadb-parser');
debug = config['debug']
url = config['url']

function log(log){
	if (debug == true){
		console.log(log)
	}
}
/*
구현해야 하는 동작
유저 - 등록 / 조회 / 정보수정

바코드 data - 등록, 조회
바코드 Raw Rait - 등록, 특정 raw의 Rait 평균, 수정 (유저 개인당 Rait는 1개로 배정하므로, 유저 개별 수정이 있어야함)
└ 바코드 data와 RawRait는 가공된 테이블에 대하여도 동일하게 적용
*/


// 유저
// 유저 등록
app.get(url['user-reg'], function(req, res) {
	var result = mysql.user_reg(db);

	var result = {
		error : false,
		result : result
	}
	log(result)
	res.send(result)
})
// 유저 조회
app.get(url['user-search'], function(req, res) {

	res.send("temp user search")
})
// 유저 수정
app.get(url['user-edit'], function(req, res) {
	res.send("temp user edit")
})

// 바코드
// 바코드 입력
app.get(url['barcode-reg'], function(req, res){
	var body = req.query
	
	var result = mysql.barcode_reg(db, body)

	log(req.query.toString() + result.toString())
	res.send(result)
})
// 바코드 조회
app.get(url['barcode-search'], function(req, res){
	res.send("temp barcode search")
})
// 바코드 평가
app.get(url['barcode-rait'], function(req, res){
	res.send("temp barcode rait")
})


// 서버 컨디션 체크
app.get(url['condition'], function(req, res){
	if (req.query['passwd'] == config['admin']['password']){
		var result = mysql.condition(db);
		log(result)

		res.send("ok")
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
