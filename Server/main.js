// express 모듈
const express = require('express')
const app = express()

// 커스텀 모듈 - config decoder
config = require('./config-decoder').open('./config.json')
db = config['db'];
port = config['port']
mysql = require('./mariadb-connector').connection(db);
parser = require('./mariadb-parser');
debug = config['debug']
url = config['url']
blacklist = config['black-list']

function log(log){
	log = "[" + new Date() + "]" + log
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
/* 유저 등록
{
	password : 평문 또는 암호화된 클라이언트에서 저장될 Password
} => 
평문 password를 Hash로 DB에 저장하며 유저 생성
리턴값은 해당 유저 name
=> {
	error : true/false
	{
		id : 유저 id 평문
		password : 저장된 Password 리턴 ( 비교용 )
	}
}
*/
app.get(url['user-reg'], function(req, res) {
	var data = req.query
	log(data)

	var result = parser.user_reg(mysql, data);
	log(result)
	res.send(result)
})
/* 유저 조회
{
	id : 조회할 유저 아이디
	password : 유저의 password
} =>
id로 검색하여 password 리턴받음
해당 password와 hash를 비교함
=> {
	error : true/false
	message : "correct user!" / "incorrect user"
}
*/
app.get(url['user-search'], function(req, res) {
	var data = req.query
	var result = parser.user_search(mysql, data)
	log(result)

	res.send(result)
})
/* 유저 수정
{
	id : 조회할 유저 아이디
	edit-id : 바꿀 아이디 명
	password : 유저의 password
} =>
*/
app.get(url['user-edit'], function(req, res) {
	var data = req.query
	var result = parser.user_edit(mysql, data, blacklist)
	log(result)

	res.send(result)
})

// 바코드
// 바코드 입력
app.get(url['barcode-reg'], function(req, res){
	var data = req.query
	var result = parser.barcode_reg(mysql, data)
	log(result)

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


/*  서버 컨디션 체크
{
	password: 컨디션 체크를 하기 위한 Password
} => "ok" or String
*/
app.get(url['condition'], function(req, res){
	if (req.query['password'] == config['admin']['password']){
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
