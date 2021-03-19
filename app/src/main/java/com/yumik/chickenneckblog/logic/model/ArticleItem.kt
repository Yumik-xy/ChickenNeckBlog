package com.yumik.chickenneckblog.logic.model

data class ArticleItem(
    val id: Int,
    val title: String,
    val container: String,
    val time: Long,
    val favoriteNumber: Long,
    val favorite: Boolean,
    val readNumber: Long,
    val image: String?,
    val authorName: String,
    val authorPicture: String?,
)