package com.yumik.chickenneckblog.logic.model

data class User(
    val id: Int,
    val name: String,
    val introduction: String,
    val picture: String?,
)