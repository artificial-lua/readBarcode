const mysql = require('mysql');

exports.host = function(json){
    var connection = mysql.createConnection({
        host     : json['host'],
        port     : json['port'],
        user     : json['user'],
        password : json['passwd'],
        database : json['db']
      });
    return pool
}