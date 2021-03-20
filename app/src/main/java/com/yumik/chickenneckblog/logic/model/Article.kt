package com.yumik.chickenneckblog.logic.model

data class Article(
    val id: Int,
    val title: String,
    val content: String,
    val time: Long,
    val readNumber: Long,
    val favoriteNumber: Long,
    val favorite: Boolean,
    val markedNumber: Long,
    val marked: Boolean,
    val followed: Boolean,
    val commentNumber: Long,
    val commentList: List<Comment>?,
    val user: User
)