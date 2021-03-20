package com.yumik.chickenneckblog.logic.response

import com.yumik.chickenneckblog.logic.model.Comment

data class CommentResponse(
    val page: Int,
    val totalPage: Int,
    val commentList: List<Comment>
)