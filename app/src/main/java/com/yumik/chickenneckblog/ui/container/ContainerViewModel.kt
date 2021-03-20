package com.yumik.chickenneckblog.ui.container

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yumik.chickenneckblog.logic.Repository

class ContainerViewModel : ViewModel() {

    private val _articleListLiveData = MutableLiveData<Int>()
    val articleListLiveData = Transformations.switchMap(_articleListLiveData) {
        Repository.getArticle(it)
    }

    fun getArticle(articleId: Int) {
        _articleListLiveData.value = articleId
    }

    fun setFollow(authorId :Int){

    }

    fun setFavourite(articleId: Int) {

    }

    fun setMark(articleId: Int){

    }
}