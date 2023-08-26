'use strict';

const app_router = require("./app.routes");
const upload_router = require("./upload.routes");

function routes(app) {
  routerConf.forEach((conf) => app.use(conf.path, conf.router));
}

//路由配置
const routerConf = [
  { path: '/', router: app_router },
  { path: '/', router: upload_router }
];

module.exports = routes;
