package com.yumik.chickenneckblog.logic.network

import com.yumik.chickenneckblog.ProjectApplication
import com.yumik.chickenneckblog.logic.response.BaseResponse
import com.yumik.chickenneckblog.logic.response.CommentResponse
import retrofit2.Call
import retrofit2.http.*

interface CommentNetwork {
    @GET("comment/check")
    fun getComment(
        @Query("articleId") articleId: Int,
        @Query("commentId") commentId: Int,
        @Query("page") page: Int
    ): Call<BaseResponse<CommentResponse>>

    @POST("comment/add")
    fun postComment(
        @Header("Authorization") Authorization: String = ProjectApplication.token
    ): Call<BaseResponse<CommentResponse>>
}