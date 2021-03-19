package com.yumik.chickenneckblog.logic.response

import com.yumik.chickenneckblog.logic.model.ArticleItem

data class ArticleResponse(
    val page: Int,
    val totalPage: Int,
    val articleList: List<ArticleItem>,
)