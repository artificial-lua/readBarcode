const mysql = require('mysql');

exports.host = function(json){
    return connection(json);
}

function connection(json){
    var connection = mysql.createConnection({
        host     : json['host'],
        port     : json['port'],
        user     : json['user'],
        password : json['passwd'],
        database : json['db']
      });
    return connection
}

exports.condition = function(json){
    var connection = connection(json);
    result = connection.query(`SHOW DATABASES`, function(error, result){
        return result;
	});
    return result
}