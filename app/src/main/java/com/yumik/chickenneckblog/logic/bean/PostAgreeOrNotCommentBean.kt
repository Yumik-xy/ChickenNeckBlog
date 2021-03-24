package com.yumik.chickenneckblog.logic.bean

data class PostAgreeOrNotCommentBean
    (
    val articleId: Int,
    val commentId: Int,
    val agree: Int, // 1 - agree 0 - none -1 - disagree
)
