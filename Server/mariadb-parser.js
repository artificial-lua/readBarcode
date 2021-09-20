const crypto = require('crypto');

exports.barcode_reg = function(mysql, body){

    return "ok";
}

exports.user_reg = function(mysql){
    var num = mysql.query('SELECT count(*) as userCount FROM user_information;')[0].userCount;
    num++;
    var hash = 'user' + num + 'hash';
    hash = crypto.createHash('sha512').update(hash).digest('base64');
    
    var result = {
        id : 'user' + num,
        hash : hash
    }
    var query = `INSERT INTO user_information VALUES (DEFAULT, '` + result['id'] + `', NOW(), 3, '` + result['hash'] + `');`;
    mysql.query(query);
    return result;
}