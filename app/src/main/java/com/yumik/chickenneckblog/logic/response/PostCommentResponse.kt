package com.yumik.chickenneckblog.logic.response

import com.yumik.chickenneckblog.logic.model.Comment

data class PostCommentResponse(
    val commentId: Int,
    val comment: Comment
)