mysql = require('./mariadb-connector');


exports.condition = function(json){
    mysql = require('./mariadb-connector');
    var conn = mysql.host(json)

    return conn.query('show databases;')
}

exports.barcode_reg = function(json, body){
    var conn = mysql.connection(json);

    return "ok"
}

exports.user_reg = function(json){
    var conn = mysql.connection(json);
    var num = conn.query('SELECT count(*) as userCount FROM user_information;')[0].userCount;
    num++;
    var result = {
        id : 'user' + num,
        hash : 'user' + num + 'hash'
    }
    var query = `INSERT INTO user_information VALUES (DEFAULT, '` + result['id'] + `', NOW(), 3, '` + result['hash'] + `');`
    conn.query(query);
    return result;
}