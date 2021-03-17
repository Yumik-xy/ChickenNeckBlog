package com.yumik.chickenneckblog.logic.response

data class LoginResponse(
    val logo: String,
    val userName: String,
    val token: String,
)