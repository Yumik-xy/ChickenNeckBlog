package com.yumik.chickenneckblog.logic

import androidx.lifecycle.liveData
import com.yumik.chickenneckblog.logic.bean.*
import com.yumik.chickenneckblog.logic.network.ProjectNetwork
import com.yumik.chickenneckblog.logic.response.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object Repository {

    private const val TAG = "Repository"

    fun getSelectedArticle(page: Int, token: String) = fire(Dispatchers.IO) {
        ProjectNetwork.getSelectedArticle(page, token)
    }

    fun getArticle(id: Int) = fire(Dispatchers.IO) {
        ProjectNetwork.getArticle(id)
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

    fun getCommentList(articleId: Int, commentId: Int, page: Int) = fire(Dispatchers.IO) {
        ProjectNetwork.getCommentList(articleId, commentId, page)
    }

    fun postComment(bean: PostCommentBean) = fire(Dispatchers.IO) {
        ProjectNetwork.postComment(bean)
    }

    fun postAgreeOrNot(bean: PostAgreeOrNotCommentBean) = fire(Dispatchers.IO) {
        ProjectNetwork.postAgreeOrNot(bean)
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