package com.yumik.chickenneckblog.logic.network

import android.util.Log
import com.yumik.chickenneckblog.logic.bean.CommentBean
import com.yumik.chickenneckblog.logic.bean.SearchBean
import com.yumik.chickenneckblog.logic.bean.TokenLoginBean
import com.yumik.chickenneckblog.logic.bean.UserLoginBean
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object ProjectNetwork {

    //    创建了接口的动态代理对象
    private val selectedArticleService = ServiceCreator.create(SelectedArticleNetwork::class.java)
    private val articleService = ServiceCreator.create(ArticleNetwork::class.java)
    private val loginService = ServiceCreator.create(LoginNetwork::class.java)
    private val searchService = ServiceCreator.create(SearchNetwork::class.java)
    private val commentNetwork = ServiceCreator.create(CommentNetwork::class.java)

    //    定义接口内容
    suspend fun getSelectedArticle(page: Int, token: String) =
        selectedArticleService.getSelectedArticle(page, token).await()

    suspend fun getArticle(id: Int) =
        articleService.getArticle(id).await()

    suspend fun login(bean: UserLoginBean) = loginService.login(bean).await()
    suspend fun loginByToken(bean: TokenLoginBean) = loginService.loginByToken(bean).await()

    suspend fun getSelectedArticle(bean: SearchBean) =
        searchService.getSelectedArticle(bean.search, bean.classify, bean.sortOrder).await()

    suspend fun getComment(bean: CommentBean) =
        commentNetwork.getComment(bean.article, bean.commentId, bean.page).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    Log.d("NetWork", response.toString())
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null.")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}