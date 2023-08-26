var express = require('express');
var router = express.Router();
const MyDB = require('../db').MyDB;

function fix_blog_content(blogs) {
  blogs.forEach(blog => {
    blog.blog_content = JSON.parse(blog.blog_content);
  });
}

router.get('/', (req, res, next) => {
  // res.send("ok");
});

// query all blog
router.get("/query_blog_all", (req, res, next) => {

  console.log("查询全部blog", req.query);

  MyDB.query_blog_all((err, blog) => {
    if (err) return next(err);
    fix_blog_content(blog);
    res.send(blog);
    return;
  });
});

// query subscribe by id
router.get("/query_subscribe", (req, res, next) => {

  console.log("查询subscribe", req.query);

  MyDB.query_subscribe(req.query.user_id, (err, result) => {
    if (err) return next(err);
    res.send(result);
    return;
  });
});

//query blog by id
router.get("/query_blog", (req, res, next) => {

  if (req.query.user_id != undefined) {

    console.log("查询blog", req.query);

    MyDB.query_blog_by_id(req.query.user_id, (err, blog) => {
      if (err) return next(err);
      fix_blog_content(blog);
      res.send(blog);
      return;
    });
  }
});


//query user by id
router.get("/query_user", (req, res, next) => {

  if (req.query.user_id != undefined) {
    console.log("查询user", req.query);
    MyDB.query_user_by_id(req.query.user_id, (err, info) => {
      if (err) return next(err);
      res.send(info);
      return;
    });
  }
});


//insert user
router.get("/insert_user", (req, res, next) => {

  console.log("添加user", req.query);

  var info = {
    user_id: req.query.user_id,
    user_password: req.query.user_password
  };

  if (info.user_id == undefined
    || info.user_password == undefined
    || info.user_id.length == 0
    || info.user_password.length == 0) {
    res.sendStatus(403);
    return;
  }

  MyDB.insert_user(info, (err) => {
    if (err) return next(err);
    res.sendStatus(200);
    return;
  });
});

//insert blog
router.get("/insert_blog", (req, res, next) => {

  console.log("添加blog", req.query);

  var info = {
    blog_user_id: req.query.blog_user_id,
    blog_title: req.query.blog_title,
    blog_content: req.query.blog_content
  };

  if (info.blog_user_id == undefined
    || info.blog_title == undefined
    || info.blog_content == undefined
    || info.blog_user_id.length == 0
    || info.blog_title.length == 0
    || info.blog_content.length == 0) {
    res.sendStatus(403);
    return;
  }

  MyDB.insert_blog(info, (err) => {
    if (err) return next(err);
    res.sendStatus(200);
    return;
  });
});


//remove blog
router.get("/remove_blog", (req, res, next) => {

  var id =req.query.id;

  if (id == undefined || id.length == 0) {
    res.sendStatus(403);
    return;
  }

  MyDB.remove_blog(id, (err) => {
    if (err) return next(err);
    res.sendStatus(200);
    return;
  });
});



//insert subscribe
router.get("/insert_subscribe", (req, res, next) => {

  console.log("添加subscribe", req.query);

  var info = {
    user_id: req.query.user_id,
    user_subscribe: req.query.user_subscribe,
  };

  if (info.user_id == undefined
    || info.user_subscribe == undefined
    || info.user_id.length == 0
    || info.user_subscribe.length == 0) {
    res.sendStatus(403);
    return;
  }

  MyDB.insert_subscribe(info, (err) => {
    if (err) return next(err);
    res.sendStatus(200);
    return;
  });
});

//remove subscribe
router.get("/remove_subscribe", (req, res, next) => {

  var info = {
    user_id: req.query.user_id,
    user_subscribe: req.query.user_subscribe,
  };

  if (info.user_id == undefined
    || info.user_subscribe == undefined
    || info.user_id.length == 0
    || info.user_subscribe.length == 0) {
    res.sendStatus(403);
    return;
  }

  MyDB.remove_subscribe(info, (err) => {
    if (err) return next(err);
    res.sendStatus(200);
    return;
  });
});

module.exports = router;
