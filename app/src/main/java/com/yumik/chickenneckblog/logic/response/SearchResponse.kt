package com.yumik.chickenneckblog.logic.response

import com.yumik.chickenneckblog.logic.model.ArticleItem

data class SearchResponse(
    val searchList: List<ArticleItem>,
)
