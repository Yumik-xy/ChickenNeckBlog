package com.yumik.chickenneckblog.ui.main.fragment.selected

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yumik.chickenneckblog.ProjectApplication
import com.yumik.chickenneckblog.logic.Repository

class SelectedViewModel : ViewModel() {

    private val _selectedArticleListLiveData = MutableLiveData<Int>().apply {
        value = 1
    }
    val selectedArticleListLiveData = Transformations.switchMap(_selectedArticleListLiveData) {
        Repository.getSelectedArticle(it, ProjectApplication.token)
    }

    /**
     * 请求精选列表
     */
    fun getSelectedArticleList(page: Int) {
        _selectedArticleListLiveData.value = page
    }
}