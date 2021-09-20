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
    var query = `INSERT INTO user_information VALUES (DEFAULT, '` + result['id'] + `', NOW(), 3, '` + result['hash'] + `');`;
    try {
        mysql.query(query);
        result = {
            error : false,
            result : result
        }
    } catch (error) {
        result = {
            error : true
        }
    }
    return result;
}

exports.user_search = function(mysql, data){
    var num = mysql.query('Select * from user_information where id="' + data['id'] + '";')
    console.log(num)

    return "temp user search"
}