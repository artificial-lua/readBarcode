const mysql = require('sync-mysql');

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
    var result = conn.query('SELECT count(*) FROM user_information;')
    return result;
}