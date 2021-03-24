package com.yumik.chickenneckblog.logic.bean

data class PostCommentBean(
    val articleId: Int,
    val commentId: Int,
    val content: String
)
