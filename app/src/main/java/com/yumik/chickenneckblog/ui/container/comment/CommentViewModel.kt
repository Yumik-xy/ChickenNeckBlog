package com.yumik.chickenneckblog.ui.container.comment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yumik.chickenneckblog.logic.Repository
import com.yumik.chickenneckblog.logic.bean.PostAgreeOrNotCommentBean
import com.yumik.chickenneckblog.logic.bean.PostCommentBean
import com.yumik.chickenneckblog.logic.enum.AgreeCode

class CommentViewModel : ViewModel() {

    data class CommentListBean(
        val articleId: Int,
        val commentId: Int,
        val page: Int
    )

    private val _commentListLiveData = MutableLiveData<CommentListBean>()
    val commentListLiveData = Transformations.switchMap(_commentListLiveData) {
        Repository.getCommentList(it.articleId, it.commentId, it.page)
    }

    fun getComment(articleId: Int, commentId: Int = -1, page: Int) {
        _commentListLiveData.value = CommentListBean(articleId, commentId, page)
    }

    private val _postCommentLiveData = MutableLiveData<PostCommentBean>()
    val postCommentLiveData = Transformations.switchMap(_postCommentLiveData) {
        Repository.postComment(it)
    }

    fun postComment(articleId: Int, commentId: Int, context: String) {
        _postCommentLiveData.value = PostCommentBean(articleId, commentId, context)
    }

    private val _postAgreeOrNotLiveData = MutableLiveData<PostAgreeOrNotCommentBean>()
    val postAgreeOrNotLiveData = Transformations.switchMap(_postAgreeOrNotLiveData) {
        Repository.postAgreeOrNot(it)
    }

    fun postAgreeOrNotLiveData(articleId: Int, commentId: Int, agree: AgreeCode) {
        _postAgreeOrNotLiveData.value =
            PostAgreeOrNotCommentBean(articleId, commentId, agree.ordinal)
    }

}