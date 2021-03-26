package com.yumik.chickenneckblog.logic.response

data class CheckUpload(
    val important: Boolean,
    val downLoadUrl: String,
    val size: Long,
    val name: String,
    val description: String,
    val version: String,
    val versionCode: Long,
    val md5: String
)