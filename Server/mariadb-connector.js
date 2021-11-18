const mysql = require('sync-mysql');

function connection(json){
    var connection = new mysql({
        host     : json['host'],
        port     : json['port'],
        user     : json['user'],
        password : json['password'],
        database : json['db'],
        charset  : 'utf8'
    });
    return connection;
}

exports.connection = function(json){

    return connection(json);
}

exports.insert = function(db, data){


    return "temp";
}

exports.count = function(db, data){

    return "temp"
}

exports.search = function(db, data){


    return "temp"
}

exports.update = function(db, data){


    return "temp"
}

const mysql = require('mysql2/promise');

class DB_master{
    constructor(json){
        this.config = json;
    }

    async select(query){
        // query = {select, table, where = {
        //     query, value
        // }, order, limit}
        let str = `SELECT `
        const values = [];
        if (query.select){
            str += `?`
            values.push(query.select);
        }else{
            str += `*`
        }
        str += ` FROM `
        str += query.table;
        if (query.where){
            str += ` WHERE `
            str += query.where.query + `=?`;
            values.push(query.where.value);
        }
        if (query.order){
            str += ` ORDER BY `
            str += query.order;
        }
        if (query.limit){
            str += ` LIMIT `
            str += query.limit;
        }

        console.log('[' + new Date() + ']' + str);
        const pool = await mysql.createPool(this.config);
        const data = await pool.query(str, values);
        
        pool.end();

        return data[0];
    }

    async insert(query){
        // query = {table, query=[], values=[]}
        let str = `INSERT INTO `
        str += query.table;

        str += ` VALUES (`
        let datas = [];
        for (let i in query.values){
            if("NOW()" == query.values[i]){
                str += `NOW(), `;
            }else if (query.values[i] == 'default' || query.values[i] == 'DEFAULT'){
                str += `DEFAULT, `;
            }else if (query.values[i] == 'NULL' || query.values[i] == 'null'){
                str += `NULL, `;
            }else{
                str += `?, `;
                datas.push(query.values[i]);
            }
        }
        str = str.slice(0, -2);
        str += `)`;

        console.log('[' + new Date() + ']' + str);
        return new Promise(async (resolve, reject) => {
            try{
                const pool = await mysql.createPool(this.config);
                const data = await pool.query(str, datas);

                pool.end();
                resolve(data[0]);
            }catch(e){
                console.log(e);
                reject(e);
            }
        });
    }
}

async function main(){
    const db = new DB_master({
        host : "127.0.0.1",
        user : "root",
        password : "test123",
        port : 3306,
        database : "ReadBarcode"
    });
    const data = await db.select({
        table : "user_information",
        where : {
            query : "id",
            value : "user3"
        },
    });
    console.log(data);


    const data2 = await db.insert({
        table : "user_information",
        values : ['default', "user9", "NOW()", 3, null]
    });
}