package com.yumik.chickenneckblog.logic.model

data class Article(
    val id: Int,
    val title: String,
    val container: String,
    val time: Long,
    val loveNumber: Int,
    val loved: Boolean,
    val readNumber: Int,
    val image: String? = null,
    val authorName: String,
    val authorPicture: String? = null,
)