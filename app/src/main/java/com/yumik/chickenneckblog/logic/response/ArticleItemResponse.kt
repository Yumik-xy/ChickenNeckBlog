package com.yumik.chickenneckblog.logic.response

import com.yumik.chickenneckblog.logic.model.ArticleItem

data class ArticleItemResponse(
    val page: Int,
    val totalPage: Int,
    val articleList: List<ArticleItem>,
)