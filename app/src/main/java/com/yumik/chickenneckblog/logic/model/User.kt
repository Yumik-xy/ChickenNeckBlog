package com.yumik.chickenneckblog.logic.model

import java.io.Serializable

data class User(
    val id: Int,
    val name: String,
    val introduction: String,
    val picture: String?,
) : Serializable