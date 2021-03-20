package com.yumik.chickenneckblog.logic.model

data class Comment(
    val id: Int,
    val time: Long,
    val content: String,
    val user: User,
    val replyTo: User,
    val agree: Int,
    val agreeNumber: Int,
    val commentNumber: Long,
    val commentList: List<Comment>?
)
