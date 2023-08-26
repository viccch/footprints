"use strict";

const sqlite3 = require('sqlite3');
const dbname = '../db/data.db';
const db = new sqlite3.Database(dbname)

// 创建表
db.serialize(() => {

    //用户表
    const sql_table_user = `
        CREATE TABLE IF NOT EXISTS table_user(
            id              INTEGER PRIMARY KEY AUTOINCREMENT,
            user_id         TEXT NOT NULL,
            user_password   TEXT NOT NULL
    );`;


    //推文表
    const sql_table_blog = `
        CREATE TABLE IF NOT EXISTS table_blog(
            id              INTEGER PRIMARY KEY AUTOINCREMENT,
            blog_time       NOT NULL DEFAULT CURRENT_TIMESTAMP,
            blog_user_id    TEXT NOT NULL,
            blog_title      TEXT NOT NULL,
            blog_content    TEXT NOT NULL
    );`;

    //订阅、粉丝表(user_id 订阅了user_subscribe，是user_subscribe的粉丝)
    const sql_table_subscribe = `
        CREATE TABLE IF NOT EXISTS table_subscribe(
            id              INTEGER PRIMARY KEY AUTOINCREMENT,
            user_id         TEXT NOT NULL,
            user_subscribe  TEXT NOT NULL
    );`;

    db.run(sql_table_user);
    db.run(sql_table_blog);
    db.run(sql_table_subscribe)
});

class MyDB {

    // query all blog
    static query_blog_all(cb) {
        db.all('SELECT * FROM table_blog', cb);
    }

    // query blog by id
    static query_blog_by_id(blog_user_id, cb) {
        db.all('SELECT * FROM table_blog WHERE blog_user_id = ?', blog_user_id, cb);
    }

    // query user by id
    static query_user_by_id(user_id, cb) {
        db.all('SELECT * FROM table_user WHERE user_id = ?', user_id, cb);
    }

    // query subscribe
    static query_subscribe(user_id, cb) {
        db.all('SELECT * FROM table_subscribe WHERE user_id = ? OR user_subscribe = ?', user_id, user_id, cb);
    }

    // insert user
    static insert_user(user_info, cb) {
        const sql_insert_user = `
                INSERT INTO
                table_user(user_id,user_password) 
                VALUES(?,?);
                SELECT LAST_INSERT_ROWID();`;
        db.run(sql_insert_user, user_info.user_id, user_info.user_password, cb);
    }
    // insert subscribe
    static insert_subscribe(subscribe_info, cb) {
        const sql_insert_subscribe = `
                        INSERT INTO
                        table_subscribe(user_id,user_subscribe) 
                        VALUES(?,?);
                        SELECT LAST_INSERT_ROWID();`;
        db.run(sql_insert_subscribe, subscribe_info.user_id, subscribe_info.user_subscribe, cb);
    }

    // insert blog
    static insert_blog(blog_info, cb) {
        const sql_insert_blog = `
                    INSERT INTO
                    table_blog(blog_user_id,blog_title,blog_content) 
                    VALUES(?,?,?);
                    SELECT LAST_INSERT_ROWID();`;
        db.run(sql_insert_blog, blog_info.blog_user_id, blog_info.blog_title, blog_info.blog_content, cb);
    }

    // delete user
    static delete_user(user_id, cb) {
        db.run(`DELETE FROM table_user WHERE user_id = ?`, user_id, cb)
    }

    // delete user
    static remove_subscribe(subscribe_info, cb) {
        db.run(`DELETE FROM table_subscribe WHERE user_id = ? AND user_subscribe = ?`, subscribe_info.user_id, subscribe_info.user_subscribe, cb)
    }

    // delete blog
    static remove_blog(id, cb) {
        db.run(`DELETE FROM table_blog WHERE id = ?`, id, cb)
    }


    // update user password
    static update_user_password(user_info, cb) {
        const sql_update_user_password = `
            UPDATE table_user
            SET user_password=?
            WHERE id=?`;
        db.run(sql_update_user_password, user_info.user_password, user_info.user_id, cb)
    }
}


module.exports.MyDB = MyDB;
