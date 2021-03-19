package com.yumik.chickenneckblog.logic.model

data class Article(
    val id: Int,
    val title: String,
    val container: String,
    val time: Long, // 暂时没用
    val readNumber: Long, // 暂时没用
    val favoriteNumber: Long,
    val favorite: Boolean,
    val markedNumber: Long,
    val marked: Boolean,
    val followed: Boolean,
    val commentNumber: Long,
    val commentList: List<Comment>?,
    val authorName: String,
    val authorIntroductionTextView: String,
    val authorPicture: String?,
)