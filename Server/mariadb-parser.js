const crypto = require('crypto');
const { query } = require('express');

exports.user_reg = function(mysql, data){
    var num = mysql.query('SELECT count(*) as userCount FROM user_information;')[0].userCount;
    num++;
    var hash = num + data['password'] + 'hash';
    hash = crypto.createHash('sha512').update(hash).digest('base64');

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
    if (value.length == 1){
        num = value[0].num
        num = num + data['password'] + 'hash';
        var hash = crypto.createHash('sha512').update(num).digest('base64')
        if (value[0].hash == hash){
            result = {
                error : false,
                message : "correct user!",
                result : value
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

function user_id_search(mysql, id){
    var value = mysql.query('Select * from user_information where id="' + id + '";')
    var result;
    if (value.error == true){
        result = {
            error : true,
            message : "DB error"
        }
    }
    else{
        if (value.length == 1){
            result = {
                error : false,
                message : "correct user"
            }
        } else{
            result = {
                error : true,
                message : "incorrect user"
            }
        }
    }

    return result
}

exports.user_edit = function(mysql, data, blacklist){
    var result;

    console.log("user-edit start")

    if (!data['id'].includes('user')){
        result = {
            error : true,
            message : "id는 1회만 변경할 수 있습니다."
        }
        return result
    }

    console.log("user-edit changed check")

    // 사용할 수 없는 id 문자열 확인
    for (const str of blacklist){
        if (data['edit-id'].includes(str)){
            result = {
                error : true,
                message : data['edit-id'] + "는 id로 사용할 수 없는 단어를 포함하고 있습니다."
            }
            return result
        }
    }

    console.log("user-edit  blacklist check")

    // DB 내 id 확인
    if (user_search(mysql, data).error == true){
        result = {
            error : true,
            message : "incorrect user. Please login again"
        }
        return result
    }
    if (user_id_search(mysql, data['edit-id']).error == false){
        result = {
            error : true,
            message : "Already used"
        }
        return result
    }

    console.log("user-edit DB check")

    // DB 수정
    var value = mysql.query('UPDATE user_information SET id="' + data['edit-id'] + '" where id="' + data['id'] + '";');
    if (value.error == true){
        result = {
            error : true,
            message : value.error
        }
    }

    if (value.changedRows != 1){
        result = {
            error : true,
            message : "Unknown error"
        }
        return value
    } else {
        result = {
            error : false,
            message : "user information update"
        }
    }
    return result;
}

exports.barcode_reg = function(mysql, data){
    var value = user_search(mysql, data)

    if (value.error){
        
    }else{

        query = 'INSERT INTO barcode_rawdata (raw, user_num, time, title, des) VALUES ("' + data.barcode + '", ' + value.result.num + ', NOW(), "' + data.title + '", "");'
        var value2 = mysql.query(query)
        if (value2.error == true){
            result = {
                error : true,
                message : "DB error"
            }
        }else{
            result = {
                error : false,
                message : "ok"
            }
        }
    }

    return result;
}

exports.barcode_search = function(mysql, data){
    var result = user_search(mysql, data)
    if (result.error) {
        
        //error
    }else{
        var value = mysql.query('select * from barcode_rawdata where raw="' + data.barcode + '";');
        
        if (value.error == true){
            result = {
                error : true,
                message : "DB error"
            }
        }else{
            if (value.length == 0){
                result = {
                    error : true,
                    message : "no item"
                }
            }else{
                result = {
                    error : false,
                    result : value
                }
            }
        }
    }

    return result;
}

exports.barcode_rait = function(mysql, data){
    var result
    


    result = {
        error : true,
        message : "temp barcode rait"
    }

    return result;
}