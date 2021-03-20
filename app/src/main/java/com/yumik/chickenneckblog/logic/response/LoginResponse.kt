package com.yumik.chickenneckblog.logic.response

import com.yumik.chickenneckblog.logic.model.User

data class LoginResponse(
    val user: User,
    val token: String,
)