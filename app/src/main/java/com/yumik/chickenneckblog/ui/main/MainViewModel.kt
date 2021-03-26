package com.yumik.chickenneckblog.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yumik.chickenneckblog.logic.Repository
import com.yumik.chickenneckblog.logic.bean.TokenLoginBean

class MainViewModel : ViewModel() {

    /**
     * 请求登录
     */
    private val _tokenLoginBeanListLiveData = MutableLiveData<TokenLoginBean>()
    val tokenLoginBeanListLiveData = Transformations.switchMap(_tokenLoginBeanListLiveData) {
        Repository.loginByToken(it)
    }

    fun loginUser(token: String) {
        _tokenLoginBeanListLiveData.value = TokenLoginBean(token)
    }

    val checkUpdateLiveData = Repository.checkUpdate()
}