package com.yumik.chickenneckblog.logic.model

data class Comment(
    val id: Int,
    val userPicture: String,
    val userName: String,
    val createTime: Long,
    val content: String,
)
