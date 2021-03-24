package com.yumik.chickenneckblog.logic.network

import com.yumik.chickenneckblog.logic.bean.PostAgreeOrNotCommentBean
import com.yumik.chickenneckblog.logic.bean.PostCommentBean
import com.yumik.chickenneckblog.logic.model.Comment
import com.yumik.chickenneckblog.logic.response.BaseResponse
import com.yumik.chickenneckblog.logic.response.CommentListResponse
import com.yumik.chickenneckblog.logic.response.PostCommentResponse
import retrofit2.Call
import retrofit2.http.*

interface CommentNetwork {
    @GET("comment/check")
    fun getCommentList(
        @Query("articleId") articleId: Int,
        @Query("commentId") commentId: Int,
        @Query("page") page: Int,
        @Header("Authorization") Authorization: String
    ): Call<BaseResponse<CommentListResponse>>

    @POST("comment/add")
    fun postComment(
        @Body bean: PostCommentBean,
        @Header("Authorization") Authorization: String
    ): Call<BaseResponse<PostCommentResponse>>

    @POST("comment/agree")
    fun postAgreeOrNot(
        @Body bean: PostAgreeOrNotCommentBean,
        @Header("Authorization") Authorization: String
    ): Call<BaseResponse<Comment>>
}