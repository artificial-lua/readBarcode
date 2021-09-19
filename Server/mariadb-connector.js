const mysql = require('sync-mysql');
const crypto = require('crypto');
const { query } = require('express');

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