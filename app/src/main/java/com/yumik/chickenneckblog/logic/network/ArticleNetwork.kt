package com.yumik.chickenneckblog.logic.network

import com.yumik.chickenneckblog.ProjectApplication
import com.yumik.chickenneckblog.logic.model.Article
import com.yumik.chickenneckblog.logic.response.ArticleResponse
import com.yumik.chickenneckblog.logic.response.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ArticleNetwork {
    @GET("article/check")
    fun getArticle(
        @Query("articleId") id: Int,
        @Header("Authorization") Authorization: String = ProjectApplication.token
    ): Call<BaseResponse<Article>>
}