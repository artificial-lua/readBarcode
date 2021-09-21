const crypto = require('crypto');

exports.user_reg = function(mysql, data){
    var num = mysql.query('SELECT count(*) as userCount FROM user_information;')[0].userCount;
    num++;
    var hash = num + data['password'] + 'hash';
    hash = crypto.createHash('sha512').update(hash).digest('base64');

    console.error(hash)
    var result = {
        id : 'user' + num,
        password : data['password']
    }
    var query = `INSERT INTO user_information VALUES (DEFAULT, '` + result['id'] + `', NOW(), 3, '` + hash + `');`;
    try {
        mysql.query(query);
        result = {
            error : false,
            result : result
        }
    } catch (error) {
        result = {
            error : true,
            message : "is error"
        }
    }
    return result;
}

exports.user_search = user_search;
function user_search(mysql, data){
    var value = mysql.query('Select * from user_information where id="' + data['id'] + '";')
    var result;
    if (value.error == false){
        num = value[0].num
        num = num + data['password'] + 'hash';
        var hash = crypto.createHash('sha512').update(num).digest('base64')
        if (value[0].hash == hash){
            result = {
                error : false,
                message : "correct user!"
            }
        } else{
            result = {
                error : true,
                message : "incorrect user"
            }
        }
    }
    else{
        result = {
            error : true,
            message : "no match"
        }
    }

    return result
}

exports.user_edit = function(mysql, data, blacklist){
    var result;

    // 사용할 수 없는 id 문자열 확인
    for (const str of blacklist){
        console.log("check " + str + " in " + data['edit-id'] + "? " + data['edit-id'].includes(str))
        if (data['edit-id'].includes(str)){
            result = {
                error : true,
                message : data['edit-id'] + "는 id로 사용할 수 없는 단어를 포함하고 있습니다."
            }
            return result
        }
    }

    // DB 내 확인
    var value = user_search(mysql, data)
    if (value.error == false){
        console.log("ok")
    }else{
        console.log("error")
    }
    return value
}

exports.barcode_reg = function(mysql, body){
    var result
    


    result = {
        error : true,
        message : "temp barcode reg"
    }

    return result;
}

exports.barcode_search = function(mysql, body){
    var result
    


    result = {
        error : true,
        message : "temp barcode search"
    }

    return result;
}

exports.barcode_rait = function(mysql, body){
    var result
    


    result = {
        error : true,
        message : "temp barcode rait"
    }

    return result;
}