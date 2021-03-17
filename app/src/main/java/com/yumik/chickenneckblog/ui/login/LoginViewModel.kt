package com.yumik.chickenneckblog.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yumik.chickenneckblog.logic.Repository
import com.yumik.chickenneckblog.logic.bean.UserLoginBean

class LoginViewModel : ViewModel() {

    /**
     * 请求登录
     */
    private val _userLoginBeanListLiveData = MutableLiveData<UserLoginBean>()
    val userLoginBeanListLiveData = Transformations.switchMap(_userLoginBeanListLiveData) {
        Repository.login(it)
    }

    fun loginUser(phone: String, password: String) {
        _userLoginBeanListLiveData.value = UserLoginBean(phone, password)
    }
}