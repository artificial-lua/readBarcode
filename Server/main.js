// express 모듈
const express = require('express')
const app = express()

// 커스텀 모듈 - config decoder
config = require('./config-decoder').open('./config.json')
db = config['db'];
port = config['port']
// mysql = require('./mariadb-connector').connection(db);
// parser = require('./mariadb-parser');
const { barcode_db_connector } = require('./maria_db_custom.js');
const { Crawler } = require('./rest.js')
debug = config['debug']
url = config['url']
blacklist = config['black-list']

const db_connector = new barcode_db_connector(db, true);
function log(log){
	log = "[" + new Date() + "] Log ::" + JSON.stringify(log)
	if (debug == true){
		console.log(log)
	}
}

function err(err){
	err = "[" + new Date() + "] Err ::" + JSON.stringify(err)
	if (debug == true){
		console.log(err)
	}
}
/*
구현해야 하는 동작
유저 - 등록 / 조회 / 정보수정

바코드 data - 등록, 조회
바코드 Raw rating - 등록, 특정 raw의 rating 평균, 수정 (유저 개인당 rating는 1개로 배정하므로, 유저 개별 수정이 있어야함)
└ 바코드 data와 Rawrating는 가공된 테이블에 대하여도 동일하게 적용
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
app.get(url['user-reg'], async function(req, res) {
	await db_connector.user_new(req.query).then(function(new_user_id){
		const result = {
			type : 'user-reg',
			error : false,
			result : {
				id : new_user_id,
				password : req.query.password
			}
		}
		res.send(result);
	}).catch(function(err){
		log(err);
		result = {
			error : true,
			result : err
		}
		res.send(result);
	})
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
	db_connector.user_search(req.query).then(function(result){
		res.send(result);
	}).catch(function(err){
		log(err);
		res.send("incorrect user");
	})
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
	//if data has edit-id, then update id
	if (data['edit-id'] != undefined){
		db_connector.user_edit(data).then(function(data){
			const result = {
				type : 'user-edit-id',
				error : false
			}
			res.send(result);
		}).catch(function(err){
			const result = {
				type : 'user-edit-id',
				error : true,
				message : err
			}
			res.send(result);
		})
	}else if (data['edit-password'] != undefined){
		db_connector.user_edit_password(data).then(function(data){
			const result = {
				type : 'user-edit-password',
				error : false
			}
			res.send(result);
		}).catch(function(err){
			const result = {
				type : 'user-edit-password',
				error : true,
				message : err
			}
			res.send(result);
		})
	}else{
		res.send("error");
	}
})

// 바코드
// 바코드 입력
app.get(url['barcode-reg'], function(req, res){
	db_connector.barcode_new(req.query).then(function(data){
		const result = {
			type : 'barcode-reg',
			error : false,
			message : data
		}
		res.send(result);
	}).catch(function(err){
		const result = {
			type : 'barcode-reg',
			error : true,
			message : err
		}
		res.send(result);
	});
})

// 바코드 조회
app.get(url['barcode-search'], async function(req, res){
	await db_connector.barcode_search(req.query).then(async function(data){
		let result;
		if(data.length == 0){
			result = {
				type : 'barcode-search',
				error : true,
				message : 'no item'
			}
		}else{
			result = {
				type : 'barcode-search',
				error : false,
				result : data[0]
			}
		}
		res.send(result);
		log(result);
	}).catch(function(err){
		log(err);
		res.send({
			type : 'barcode-search',
			error : true,
			message : err
		});
	});
})
// 바코드 평가
// app.get(url['barcode-rating'], function(req, res){
// 	var data = req.query
// 	var result = parser.barcode_rating(mysql, data)
// 	log(result)

// 	res.send(result)
// })


/*  서버 컨디션 체크
*/
app.get(url['condition'], function(req, res){
	res.send("ok")
})

// 마지막으로, 포트로 열어줍니다.
app.listen(port, function(){
	log('listening on port ' + port + '!');
	})
