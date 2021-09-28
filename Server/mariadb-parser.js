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
                result : value[0]
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


    if (!data['id'].includes('user')){
        result = {
            error : true,
            message : "id는 1회만 변경할 수 있습니다."
        }
        return result
    }


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

    var result

    if (value.error){
        result = {
            error : true,
            message : value
        }        
    }else{

        qr = 'INSERT INTO barcode_rawdata VALUES (DEFAULT, "' + data.barcode + '", ' + value.result.num + ', NOW(), "' + data.title + '", "");';
        var dbresult = mysql.query(qr)
        if (dbresult.error == true){
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
                    result : value[0]
                }
            }
        }
    }

    return result;
}

exports.barcode_rating = function(mysql, data){
    var result
    var qr
    var qr2

    var value = user_search(mysql, data);
    if(value.error){
        result = {
            error : true,
            message : "user error"
        }
    }else{
        var user_num = value.result.num
        if(data.kind == 'raw'){
            qr = 'select * from barcode_rawdata where id=' + data.barcodeid + ';';
            qr2 = 'select * from barcode_rating where user_num=' + user_num;
        }else if(data.kind == 'processed'){
            qr = 'select * from processed_data where id=' + data.barcodeid + ';';
            qr2 = 'select * from processed_rating where user_num=' + user_num;
        }else{
            result = {
                error : true,
                message : "kind error"
            }
            return result
        }
        value = mysql.query(qr);

        if(value.error){
            result = {
                error : true,
                message : "data number error"
            }
        }else{
            value = mysql.query(qr2);
            console.log(value)

            if (value.error){
                result = {
                    error : true,
                    message : "DB error"
                }
            }else if(value.length != 0){
                value = value[0]
                if(data.kind == 'raw'){
                    qr = 'UPDATE barcode_rating SET rating=' + data['rating'] + ' where num="' + value['num'] + '";';
                }else{
                    qr = 'UPDATE processed_rating SET rating=' + data['rating'] + ' where num="' + value['num'] + '";';
                }
                console.log(qr);
                value = mysql.query(qr);

                if (value.error){
                    result = {
                        error : true,
                        message : "update error"
                    }
                }
                else {
                    result = {
                        error : false,
                        message : "update complete"
                    }
                }
            }else{
                if(data.kind == 'raw'){
                    qr = 'insert into barcode_rating VALUES(DEFAULT, ' + data.barcodeid + ', ' + user_num + ', ' + data.rating + ', NOW());'
                }else{
                    qr = 'insert into processed_rating VALUES(DEFAULT, ' + data.barcodeid + ', ' + user_num + ', ' + data.rating + ', NOW());'
                }

                value = mysql.query(qr);
                if (value.error){
                    result = {
                        error : true,
                        message : "insert error"
                    }
                }else{
                    result = {
                        error : false,
                        message : "평가 완료"
                    }
                }
            }
        }
    }

    return result;
}