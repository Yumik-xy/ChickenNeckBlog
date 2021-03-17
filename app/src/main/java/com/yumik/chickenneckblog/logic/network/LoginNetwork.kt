package com.yumik.chickenneckblog.logic.network

import com.yumik.chickenneckblog.ProjectApplication
import com.yumik.chickenneckblog.logic.bean.TokenLoginBean
import com.yumik.chickenneckblog.logic.bean.UserLoginBean
import com.yumik.chickenneckblog.logic.response.ArticleResponse
import com.yumik.chickenneckblog.logic.response.BaseResponse
import com.yumik.chickenneckblog.logic.response.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface LoginNetwork {
    @POST("user/login")
    fun login(
        @Body bean: UserLoginBean,
    ): Call<BaseResponse<LoginResponse>>

    @POST("user/login/token")
    fun loginByToken(
        @Body bean: TokenLoginBean,
    ): Call<BaseResponse<LoginResponse>>
}