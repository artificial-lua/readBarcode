const mysql = require('sync-mysql');
const crypto = require('crypto');

function connection(json){
    var connection = new mysql({
        host     : json['host'],
        port     : json['port'],
        user     : json['user'],
        password : json['passwd'],
        database : json['db']
    });
    return connection
}

exports.connection = function(json){

    return connection(json);
}

exports.host = function(json){

    return connection(json);
}

exports.condition = function(json){

    return connection(json).query('show databases;')
}

exports.user_reg = function(json){
    var conn = connection(json);
    var num = conn.query('SELECT count(*) as userCount FROM user_information;')[0].userCount;
    num++;
    var result = {
        id = 'user' + num,
        hash = 'user' + num + 'hash'
    }
    conn.query(`
    INSERT INTO user_information
    (id, 
    signup, 
    group, 
    hash)
VALUES
    ('` + result['id'] + `', 
    NOW(), 
    3, 
    '` + result['hash'] + `');
    `);
    return result;
}