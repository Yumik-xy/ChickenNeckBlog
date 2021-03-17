package com.yumik.chickenneckblog.ui.main.fragment.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yumik.chickenneckblog.logic.Repository
import com.yumik.chickenneckblog.logic.bean.SearchBean

class SearchViewModel : ViewModel() {


    private val _searchArticleListLiveData = MutableLiveData<SearchBean>()

    val searchArticleListLiveData = Transformations.switchMap(_searchArticleListLiveData) {
        Repository.getSelectedArticle(it)
    }

    /**
     * 搜索列表
     */
    fun searchArticleList(search: String, classify: String, sortOrder: String) {
        _searchArticleListLiveData.value = SearchBean(search, classify, sortOrder)
    }
}