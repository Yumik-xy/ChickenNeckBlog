package com.yumik.chickenneckblog.logic.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface DownLoadNetwork {
    @Streaming
    @GET
    fun download(@Url url: String): Call<ResponseBody>
}