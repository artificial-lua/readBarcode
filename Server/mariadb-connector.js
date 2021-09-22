const mysql = require('sync-mysql');

function connection(json){
    var connection = new mysql({
        host     : json['host'],
        port     : json['port'],
        user     : json['user'],
        password : json['password'],
        database : json['db']
    });
    return connection;
}

exports.connection = function(json){

    return connection(json);
}

exports.insert = function(db, json){


    return "temp";
}

exports.count = function(db, json){


    return "temp"
}

exports.search = function(db, json){


    return "temp"
}

exports.update = function(db, json){


    return "temp"
}