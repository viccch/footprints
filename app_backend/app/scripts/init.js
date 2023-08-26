"use strict";

var test_data = {
    "table_user": [
        { "user_id": "admin", "user_password": "123456" },
        { "user_id": "viccch", "user_password": "123456" },
        { "user_id": "123456", "user_password": "123456" }
    ],
    "table_subscribe": [
        { "user_id": "admin", "user_subscribe": "viccch" },
        { "user_id": "admin", "user_subscribe": "123456" },
        { "user_id": "viccch", "user_subscribe": "admin" }
    ],
    "table_blog": [
        {
            "blog_user_id": "viccch",
            "blog_title": "test__1",
            "blog_content": {
                "head_image_url": "http://192.168.31.223:3000/res/admin/test/eg_tulip.jpg",
                "start_time": "2023-08-14 17:57:30",
                "seconds": 110,
                "location":"河南省 南阳市 内乡县",
                "distance":1.10,
                "markers": [
                    {
                        "title": "test_1",
                        "description": "test_1_description",
                        "time": "2023-08-14 17:58:49",
                        "position": [
                            112.003768,
                            33.212134,
                            2.0
                        ],
                        "mime_items": [
                            {
                                "type": "video/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/movie1.mp4"
                            },
                            {
                                "type": "image/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/eg_tulip.jpg"
                            }
                        ]
                    },
                    {
                        "title": "test_2",
                        "description": "test_2_description",
                        "time": "2023-08-15 17:58:49",
                        "position": [
                            111.994069,
                            33.209405,
                            3.0
                        ],
                        "mime_items": [
                            {
                                "type": "image/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/eg_tulip.jpg"
                            },
                            {
                                "type": "video/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/movie1.mp4"
                            }
                        ]
                    }
                ],
                "line_string": [
                    [
                        111.985057,
                        33.214791,
                        1.0
                    ],
                    [
                        112.003768,
                        33.212134,
                        2.0
                    ],
                    [
                        111.994069,
                        33.209405,
                        3.0
                    ],
                    [
                        111.992267,
                        33.204019,
                        4.0
                    ]
                ]
            }
        }
        ,
        {
            "blog_user_id": "viccch",
            "blog_title": "test__1",
            "blog_content": {
                "head_image_url": "http://192.168.31.223:3000/res/admin/test/eg_tulip.jpg",
                "start_time": "2023-08-14 17:57:30",
                "seconds": 110,
                "location":"河南省 南阳市 内乡县",
                "distance":1.10,
                "markers": [
                    {
                        "title": "test_1",
                        "description": "test_1_description",
                        "time": "2023-08-14 17:58:49",
                        "position": [
                            112.003768,
                            33.212134,
                            2.0
                        ],
                        "mime_items": [
                            {
                                "type": "video/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/movie1.mp4"
                            },
                            {
                                "type": "image/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/eg_tulip.jpg"
                            }
                        ]
                    },
                    {
                        "title": "test_2",
                        "description": "test_2_description",
                        "time": "2023-08-15 17:58:49",
                        "position": [
                            111.994069,
                            33.209405,
                            3.0
                        ],
                        "mime_items": [
                            {
                                "type": "image/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/eg_tulip.jpg"
                            },
                            {
                                "type": "video/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/movie1.mp4"
                            }
                        ]
                    }
                ],
                "line_string": [
                    [
                        111.985057,
                        33.214791,
                        1.0
                    ],
                    [
                        112.003768,
                        33.212134,
                        2.0
                    ],
                    [
                        111.994069,
                        33.209405,
                        3.0
                    ],
                    [
                        111.992267,
                        33.204019,
                        4.0
                    ]
                ]
            }
        }
        ,    {
            "blog_user_id": "viccch",
            "blog_title": "test__1",
            "blog_content": {
                "head_image_url": "http://192.168.31.223:3000/res/admin/test/eg_tulip.jpg",
                "start_time": "2023-08-14 17:57:30",
                "seconds": 110,
                "location":"河南省 南阳市 内乡县",
                "distance":1.10,
                "markers": [
                    {
                        "title": "test_1",
                        "description": "test_1_description",
                        "time": "2023-08-14 17:58:49",
                        "position": [
                            112.003768,
                            33.212134,
                            2.0
                        ],
                        "mime_items": [
                            {
                                "type": "video/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/movie1.mp4"
                            },
                            {
                                "type": "image/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/eg_tulip.jpg"
                            }
                        ]
                    },
                    {
                        "title": "test_2",
                        "description": "test_2_description",
                        "time": "2023-08-15 17:58:49",
                        "position": [
                            111.994069,
                            33.209405,
                            3.0
                        ],
                        "mime_items": [
                            {
                                "type": "image/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/eg_tulip.jpg"
                            },
                            {
                                "type": "video/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/movie1.mp4"
                            }
                        ]
                    }
                ],
                "line_string": [
                    [
                        111.985057,
                        33.214791,
                        1.0
                    ],
                    [
                        112.003768,
                        33.212134,
                        2.0
                    ],
                    [
                        111.994069,
                        33.209405,
                        3.0
                    ],
                    [
                        111.992267,
                        33.204019,
                        4.0
                    ]
                ]
            }
        }
        ,
        {
            "blog_user_id": "viccch",
            "blog_title": "test__1",
            "blog_content": {
                "head_image_url": "http://192.168.31.223:3000/res/admin/test/eg_tulip.jpg",
                "start_time": "2023-08-14 17:57:30",
                "seconds": 110,
                "location":"河南省 南阳市 内乡县",
                "distance":1.10,
                "markers": [
                    {
                        "title": "test_1",
                        "description": "test_1_description",
                        "time": "2023-08-14 17:58:49",
                        "position": [
                            112.003768,
                            33.212134,
                            2.0
                        ],
                        "mime_items": [
                            {
                                "type": "video/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/movie1.mp4"
                            },
                            {
                                "type": "image/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/eg_tulip.jpg"
                            }
                        ]
                    },
                    {
                        "title": "test_2",
                        "description": "test_2_description",
                        "time": "2023-08-15 17:58:49",
                        "position": [
                            111.994069,
                            33.209405,
                            3.0
                        ],
                        "mime_items": [
                            {
                                "type": "image/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/eg_tulip.jpg"
                            },
                            {
                                "type": "video/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/movie1.mp4"
                            }
                        ]
                    }
                ],
                "line_string": [
                    [
                        111.985057,
                        33.214791,
                        1.0
                    ],
                    [
                        112.003768,
                        33.212134,
                        2.0
                    ],
                    [
                        111.994069,
                        33.209405,
                        3.0
                    ],
                    [
                        111.992267,
                        33.204019,
                        4.0
                    ]
                ]
            }
        }
        ,    {
            "blog_user_id": "viccch",
            "blog_title": "test__1",
            "blog_content": {
                "head_image_url": "http://192.168.31.223:3000/res/admin/test/eg_tulip.jpg",
                "start_time": "2023-08-14 17:57:30",
                "seconds": 110,
                "location":"河南省 南阳市 内乡县",
                "distance":1.10,
                "markers": [
                    {
                        "title": "test_1",
                        "description": "test_1_description",
                        "time": "2023-08-14 17:58:49",
                        "position": [
                            112.003768,
                            33.212134,
                            2.0
                        ],
                        "mime_items": [
                            {
                                "type": "video/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/movie1.mp4"
                            },
                            {
                                "type": "image/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/eg_tulip.jpg"
                            }
                        ]
                    },
                    {
                        "title": "test_2",
                        "description": "test_2_description",
                        "time": "2023-08-15 17:58:49",
                        "position": [
                            111.994069,
                            33.209405,
                            3.0
                        ],
                        "mime_items": [
                            {
                                "type": "image/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/eg_tulip.jpg"
                            },
                            {
                                "type": "video/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/movie1.mp4"
                            }
                        ]
                    }
                ],
                "line_string": [
                    [
                        111.985057,
                        33.214791,
                        1.0
                    ],
                    [
                        112.003768,
                        33.212134,
                        2.0
                    ],
                    [
                        111.994069,
                        33.209405,
                        3.0
                    ],
                    [
                        111.992267,
                        33.204019,
                        4.0
                    ]
                ]
            }
        }
        ,
        {
            "blog_user_id": "admin",
            "blog_title": "test__1",
            "blog_content": {
                "head_image_url": "http://192.168.31.223:3000/res/admin/test/eg_tulip.jpg",
                "start_time": "2023-08-14 17:57:30",
                "seconds": 110,
                "location":"河南省 南阳市 内乡县",
                "distance":1.10,
                "markers": [
                    {
                        "title": "test_1",
                        "description": "test_1_description",
                        "time": "2023-08-14 17:58:49",
                        "position": [
                            112.003768,
                            33.212134,
                            2.0
                        ],
                        "mime_items": [
                            {
                                "type": "video/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/movie1.mp4"
                            },
                            {
                                "type": "image/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/eg_tulip.jpg"
                            }
                        ]
                    },
                    {
                        "title": "test_2",
                        "description": "test_2_description",
                        "time": "2023-08-15 17:58:49",
                        "position": [
                            111.994069,
                            33.209405,
                            3.0
                        ],
                        "mime_items": [
                            {
                                "type": "image/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/eg_tulip.jpg"
                            },
                            {
                                "type": "video/*",
                                "url": "http://192.168.31.223:3000/res/admin/test/movie1.mp4"
                            }
                        ]
                    }
                ],
                "line_string": [
                    [
                        111.985057,
                        33.214791,
                        1.0
                    ],
                    [
                        112.003768,
                        33.212134,
                        2.0
                    ],
                    [
                        111.994069,
                        33.209405,
                        3.0
                    ],
                    [
                        111.992267,
                        33.204019,
                        4.0
                    ]
                ]
            }
        }
    ]
};



const load_table_user = () => {

    for (var i in test_data.table_user) {
        var info = test_data.table_user[i];
        var url = "http://localhost:3000/insert_user?user_id=" + info.user_id + "&user_password=" + info.user_password;
        fetch(url);
    }
};

const load_table_blog = () => {

    for (var i in test_data.table_blog) {
        var info = test_data.table_blog[i];
        var url = "http://localhost:3000/insert_blog?blog_user_id=" + info.blog_user_id + "&blog_title=" + info.blog_title + "&blog_content=" + JSON.stringify(info.blog_content);
        fetch(url);
    }
};

const load_table_subscribe = () => {

    for (var i in test_data.table_subscribe) {
        var info = test_data.table_subscribe[i];
        var url = "http://localhost:3000/insert_subscribe?user_id=" + info.user_id + "&user_subscribe=" + info.user_subscribe;
        fetch(url);
    }
};


load_table_user();
load_table_blog();
load_table_subscribe();

