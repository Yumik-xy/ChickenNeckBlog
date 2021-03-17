package com.yumik.chickenneckblog.logic.enum

enum class ErrCode(val errCode: Int) {
    TOKEN_ERROR(10000),
    USER_UN_EXIST(10001),
    PASSWORD_ERROR(10002),
    USER_EXIST(10003),
    NICKNAME_EXIST(10004),
    PASSWORD_CHECK_ERROR(10005),

    ARTICLE_UN_EXIST(20000)
}