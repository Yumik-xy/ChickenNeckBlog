package com.yumik.chickenneckblog.logic.network

import com.yumik.chickenneckblog.logic.response.SearchResponse
import com.yumik.chickenneckblog.logic.response.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchNetwork {
    @GET("article/search")
    fun getSelectedArticle(
        @Query("search") search: String,
        @Query("classify") classify: String,
        @Query("sortOrder") sortOrder: String,
        @Header("Authorization") Authorization: String
    ): Call<BaseResponse<SearchResponse>>
}