const crypto = require('crypto');
const { DB_master } = require('./maria_db_master.js');

class barcode_db_connector{
    constructor(json, debug = false){
        this.conn = new DB_master(json, debug);
    }

    make_hash(password, salt='salt'){
        /*
        make hash from password and salt
        */
        return crypto.createHash('sha256').update(password + salt).digest('hex');
    }

    async user_new(json){
        /*
        create new user in user_information table
        json = {
            password
        }
        */
        
        const user_num = await this.conn.count({
            table: 'user_information'
        });

        const new_user_id = 'user' + (user_num + 1);

        const hash = this.make_hash(json.password);

        return new Promise((resolve, reject) => {
            this.conn.insert({
                table: 'user_information',
                values: ['DEFAULT', new_user_id, 'NOW()', 3, hash]
            }).then(() => {
                resolve(new_user_id);
            }).catch(err => {
                reject("user_new error");
            });
        });
    }

    async user_login(json){
        /*
        json = {
            id
            password
        }
        */
        const hash = this.make_hash(json.password);

        return new Promise((resolve, reject) => {
            this.conn.select({
                table : "user_information",
                where : {
                    query : "id",
                    value : json.id
                }
            }).then(data => {
                if(data.length == 0){
                    reject("user not found");
                }else{
                    if(data[0].hash == hash){
                        resolve(data[0]);
                    }else{
                        reject("password is not correct");
                    }
                }
            }).catch(err => {
                reject(err);
            });
        });
    }

    async user_search(json){
        return new Promise((resolve, reject) => {
            this.conn.select({
                table : "user_information",
                where : {
                    query : "id",
                    value : json.id
                }
            }).then(data => {
                if(data == null){
                    reject("user not found");
                }else{
                    if(data[0].hash == this.make_hash(json.password)){
                        resolve(data[0]);
                    }else{
                        reject("password is not correct");
                    }
                }
            }).catch(err => {
                reject(err);
            });
        });
    }

    async user_update_password(json){
        /*
        json = {
            id,
            password,
            new_password
        }
        */

        const hash = this.make_hash(json.password);

        let correct = this.user_login({
            id: json.id,
            password: json.password
        }).then(data => {
            return true;
        }
        ).catch(err => {
            return false;
        });

        if(correct){
            const new_hash = this.make_hash(json.new_password);
            return new Promise((resolve, reject) => {
                this.conn.update({
                    table : "user_information",
                    set : {
                        query : ["hash"],
                        value : [new_hash]
                    },
                    where : {
                        query : "id",
                        value : json.id
                    }
                }).then(data => {
                    resolve(data);
                }).catch(err => {
                    reject(err);
                });
            });
        }
    }

    async user_update_id(json){
        /*
        json = {
            id,
            new_id,
            password
        }
        */
        return new Promise((resolve, reject) => {
            // if id include 'user' then update id to new_id, else reject
            if(json.id.indexOf('user') == 0){
                this.conn.update({
                    table : "user_information",
                    set : {
                        query : ["id"],
                        value : [json.new_id]
                    },
                    where : {
                        query : "id",
                        value : json.id
                    }
                }).then(data => {
                    resolve(data);
                }).catch(err => {
                    this.conn.err(err);
                    reject(err);
                });
            }else{
                reject("id is not correct");
            }
        });
    }

    async barcode_new(json){
        return new Promise((resolve, reject) => {
            //check correct user by user_search
            this.user_search({
                id: json.id,
                password: json.password
            }).then(data => {
                //insert new barcode in barcode_rawdata table
                /*
                    `id`        INT            NOT NULL    AUTO_INCREMENT COMMENT 'Barcode_ID', 
                    `raw`       VARCHAR(45)    NULL        COMMENT 'Barcode_raw', 
                    `user_num`  INT            NULL        COMMENT 'User_Number', 
                    `time`      TIMESTAMP      NULL        COMMENT 'Date_Time', 
                    `title`     VARCHAR(45)    NULL        COMMENT 'Barcode_title', 
                    `des`       VARCHAR(45)    NULL        COMMENT 'Descript', 
                    CONSTRAINT PK_barcode_rawdata PRIMARY KEY (id),
                    FOREIGN KEY (`user_num`) REFERENCES `user_information` (`num`)
                */
                this.conn.insert({
                    table: 'barcode_rawdata',
                    values: ['DEFAULT', json.barcode, data.num, 'NOW()', json.title, json.des]
                }).then(() => {
                    resolve("barcode inserted");
                }).catch(err => {
                    reject(err);
                });
            }).catch(err => {
                reject(err);
            });
        });
    }

    async barcode_search(json){
        return new Promise((resolve, reject) => {
            //check correct user by user_search
            this.user_search({
                id: json.id,
                password: json.password
            }).then(data => {
                //select barcode by raw from barcode_rawdata table
                this.conn.select({
                    table : "barcode_rawdata",
                    where : {
                        query : "raw",
                        value : json.barcode
                    }
                }).then(result => {
                    console.log(result)
                    resolve(result);
                }).catch(err => {
                    reject(err);
                });
            }).catch(err => {
                reject(err);
            });
        });
    }
}

exports.barcode_db_connector = barcode_db_connector;

async function main(){
    const db = new barcode_db_connector({
        host : "127.0.0.1",
        user : "root",
        password : "test123",
        port : 3306,
        database : "ReadBarcode"
    }, true);

    // let data = (await db.conn.select({
    //     table : "user_information",
    //     where : {
    //         query : "id",
    //         value : "user3"
    //     },
    // }))[0];
    // console.log("result : ", data);

    // data = await db.conn.count({
    //     table : "user_information"
    // });
    // console.log("result : ", data);

    // data = await db.conn.update({
    //     table : "user_information",
    //     set : {
    //         query : ["hash"],
    //         value : ["12345"]
    //     },
    //     where : {
    //         query : "id",
    //         value : "user3"
    //     }
    // });

    // console.log("result : ", data);

    // await db.user_new({
    //         password : "testpass1234"
    //     }).then(data => {
    //         console.log("result : ", data);
    //         abc(data);
    //     }).catch(err => {
    //         console.log(err);
    //     }
    // );

    // await db.user_update_password({
    //     id : "user22",
    //     password : "testpass1234",
    //     new_password : "testpass12345"
    // }).then(data => {
    //     console.log("result : ", data);
    // }).catch(err => {
    //     console.log(err);
    // });

    // await db.user_update_id({
    //     id : "user22",
    //     new_id : "test22",
    //     password : "testpass12345"}).then(data => {
    //     console.log("result : ", data);
    // }).catch(err => {
    //     console.log(err);
    // });

    // await db.user_search({
    //     id : "user22"
    // }).then(data => {
    //     console.log("result : ", data);
    // }).catch(err => {
    //     console.log(err);
    // });

    // await db.user_search({
    //     id : "test22"
    // }).then(data => {
    //     console.log("result : ", data);
    // }).catch(err => {
    //     console.log(err);
    // });

    // await db.barcode_new({
    //     id : "user21",
    //     password : "testpass1234",
    //     barcode : "1234567890123",
    //     title : "test title"
    // }).then(data => {
    //     console.log("result : ", data);
    // }).catch(err => {
    //     console.log(err);
    // });


    // await db.barcode_search({
    //     id : "user21",
    //     password : "testpass1234",
    //     barcode : "123456789012"
    // }).then(data => {
    //     console.log("result : ", data);
    // }).catch(err => {
    //     console.log(err);
    // });
}

// main();