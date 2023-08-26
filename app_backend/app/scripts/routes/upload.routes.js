"use strict";

const express = require('express');
const multer = require("multer");
const path = require('path');
const fs = require('fs');
// const gm = requireg('gm');
const router = express.Router();

var storage = multer.diskStorage({
    // 配置文件上传后存储的路径
    destination: (req, file, cb) => {
        var path_to_save = path.join('../upload/', req.body.user_id, req.body.project_id);

        //确保目录存在
        fs.mkdirSync(path_to_save, { recursive: true });

        // //生成缩略图
        // fs.mkdirSync(path.join(path_to_save, 'thumb'), { recursive: true });
        // gm(path.join(path_to_save, file.originalname))
        //     .resize(50, 50)
        //     .write(path.join(path_to_save, "thumb", file.originalname), (err) => { if (err) { console.log(err); } });

        cb(null, path_to_save);
    },
    // 配置文件上传后存储的路径和文件名
    filename: (req, file, cb) => {
        cb(null, file.originalname); //文件名
    }
})

// 添加配置文件到muler对象。
var upload = multer({ storage: storage });

/***
 * 文件上传，并将拼接好的url返回给前端，示例
 */
router.post('/upload_file', upload.single('file'), (req, res) => {

    console.log("upload_file\t", req.file?.originalname);

    res.json({
        code: 200,
        data: {
            // @ts-ignore
            url: `/res/${req.body.user_id}/${req.body.project_id}/${req.file.filename}`
        },
        msg: 'success'
    });
})

module.exports = router