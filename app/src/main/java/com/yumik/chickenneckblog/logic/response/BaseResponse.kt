package com.yumik.chickenneckblog.logic.response

data class BaseResponse<T>(
    val success: Boolean,
    val code: Int,
    val message: String,
    val data: T?,
)