package com.yumik.chickenneckblog.logic.model

data class SelectedComment(
    val id: Int,
    val userPicture: String?,
    val userName: String,
    val replyToUserName: String?,
    val createTime: Long,
    val content: String,
)
