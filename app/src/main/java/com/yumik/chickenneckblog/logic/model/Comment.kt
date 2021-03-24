package com.yumik.chickenneckblog.logic.model

import java.io.Serializable

data class Comment(
    val id: Int,
    val time: Long,
    val content: String,
    val user: User,
    val replyTo: User,
    val agree: Int,
    val agreeNumber: Int,
    val commentNumber: Long,
    var commentList: MutableList<Comment>?
) : Serializable
