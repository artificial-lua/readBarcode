const mysql = require('mysql2/promise');

class DB_master{
    constructor(json, debug = false){
        /*
        json = {
            host,
            user,
            password,
            port,
            database
        */
        this.config = json;
        this.debug = debug;
    }

    async select(query){
        /*
        query = {
            select,
            table,
            where = {
                query,
                value
            },
            order,
            limit
        }
        */
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

        this.log(str);
        this.log(values);

        return new Promise(async (resolve, reject) => {
            let pool;
            try{

                pool = await mysql.createPool(this.config);
                const data = await pool.query(str, values);
                pool.end();
                resolve(data[0]);
            }catch(err){
                pool.end();
                this.err(err);
                reject(err);
            }
        });
    }

    async insert(query){
        /*
        query = {
            table,
            values=[]
        }
        */
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

        this.log(str);
        this.log(datas);
        return new Promise(async (resolve, reject) => {
            let pool
            try{
                pool = await mysql.createPool(this.config);
                const data = await pool.query(str, datas);

                pool.end();
                resolve(data[0]);
            }catch(e){
                pool.end();
                console.log(e)
                this.err(e.errno);
                reject(e.errno);
            }
        });
    }

    async update(query){
        /*
        query = {
            table,
            set : {
                query : [],
                value : []
            }
            where : {
                query,
                value
            }
        }*/
        let str = `UPDATE `
        str += query.table;
        str += ` SET `;
        for (let i in query.set.query){
            str += query.set.query[i] + `=?, `;
        }
        str = str.slice(0, -2);

        str += ` WHERE `
        str += query.where.query + `=?`;
        const values = [];
        for (let i in query.set.value){
            values.push(query.set.value[i]);
        }
        values.push(query.where.value);

        this.log(str);
        return new Promise(async (resolve, reject) => {
            let pool;
            try{
                pool = await mysql.createPool(this.config);
                const data = await pool.query(str, values);

                pool.end();
                resolve(data[0]);
            }catch(e){
                pool.end();
                this.log(e);
                reject(e);
            }
        });
    }

    async count(query){
        let str = `SELECT count(*) as count FROM `
        str += query.table;
        const values = [];
        if (query.where){
            str += ` WHERE `
            str += query.where.query + `=?`;
            values.push(query.where.value);
        }

        this.log(str);

        return new Promise(async (resolve, reject) => {
            try{
                const pool = await mysql.createPool(this.config);
                const data = await pool.query(str, values);
                pool.end();
                resolve(data[0][0].count);
            }catch(e){
                this.log(e);
                reject(e);
            }
        });
    }

    log(s){
        if (this.debug){
            console.log('[' + new Date() + '] Log :: ' + s);
        }
    }
    err(e){
        console.error('[' + new Date() + '] Err :: ' + e);
    }
}

exports.DB_master = DB_master;