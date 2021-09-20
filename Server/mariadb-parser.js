const crypto = require('crypto');

exports.barcode_reg = function(mysql, body){

    return "ok";
}

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

exports.user_search = function(mysql, data){
    var num = mysql.query('Select num from user_information where id="' + data['id'] + '";')
    console.log(num)
    var result;
    if (num.lenght() == 1){
        num = num[0].num
        num = num + data['password'] + 'hash';
        var hash = crypto.createHash('sha512').update(num).digest('base64')
        result = mysql.query('Select * from user_information where id="' + data['id'] + '" and hash="' + hash + '";');
        console.log(result)
    }
    else{
        result = {
            error : true,
            message : "no match"
        }
    }

    return result
}