package com.yumik.chickenneckblog.ui.container.comment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yumik.chickenneckblog.logic.Repository
import com.yumik.chickenneckblog.logic.bean.CommentBean

class CommentViewModel : ViewModel() {

    private val _commentListLiveData = MutableLiveData<CommentBean>()
    val commentListLiveData = Transformations.switchMap(_commentListLiveData) {
        Repository.getComment(it)
    }

    fun getComment(articleId: Int, commentId: Int = -1, page: Int) {
        _commentListLiveData.value = CommentBean(articleId, commentId, page)
    }

}