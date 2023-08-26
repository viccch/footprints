"use strict";

const multer = require('multer');
const createError = require('http-errors');
const path = require('path');
const express = require('express');
const cors = require('cors');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const routes = require('./routes/index');

const app = express();

app.use(cors());
app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, '../../public')));
app.use("/res",express.static(path.join(__dirname, '../../upload')));

const PORT = 3000;

app.listen(PORT, async () => {
    console.log(`App is running at http://127.0.0.1:${PORT}`);
    routes(app);
});
