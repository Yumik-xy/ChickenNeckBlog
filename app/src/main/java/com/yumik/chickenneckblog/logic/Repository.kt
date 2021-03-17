package com.yumik.chickenneckblog.logic

import androidx.lifecycle.liveData
import com.yumik.chickenneckblog.logic.bean.SearchBean
import com.yumik.chickenneckblog.logic.bean.TokenLoginBean
import com.yumik.chickenneckblog.logic.bean.UserLoginBean
import com.yumik.chickenneckblog.logic.network.ProjectNetwork
import com.yumik.chickenneckblog.logic.response.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object Repository {

    private const val TAG = "Repository"

    fun getSelectedArticle(page: Int, token: String) = fire(Dispatchers.IO) {
        ProjectNetwork.getSelectedArticle(page, token)
    }

    fun login(bean: UserLoginBean) = fire(Dispatchers.IO) {
        ProjectNetwork.login(bean)
    }

    fun loginByToken(bean: TokenLoginBean) = fire(Dispatchers.IO) {
        ProjectNetwork.loginByToken(bean)
    }

    fun getSelectedArticle(bean: SearchBean) = fire(Dispatchers.IO) {
        ProjectNetwork.getSelectedArticle(bean)
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> BaseResponse<T>) =
        liveData(context) {
            val result = try {
                val returnResult = block()
                Result.success(returnResult)
            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result)
        }
}