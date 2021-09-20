exports.condition = function(mysql){

    return mysql.query('show databases;')
}

exports.barcode_reg = function(mysql, body){

    return "ok"
}

exports.user_reg = function(mysql){
    var num = mysql.query('SELECT count(*) as userCount FROM user_information;')[0].userCount;
    num++;
    var result = {
        id : 'user' + num,
        hash : 'user' + num + 'hash'
    }
    var query = `INSERT INTO user_information VALUES (DEFAULT, '` + result['id'] + `', NOW(), 3, '` + result['hash'] + `');`
    mysql.query(query);
    return result;
}