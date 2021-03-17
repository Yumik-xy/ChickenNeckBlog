package com.yumik.chickenneckblog.ui.container

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yumik.chickenneckblog.logic.model.SelectedComment

class ContainerViewModel : ViewModel() {

    private val _selectedCommentListLiveData =
        MutableLiveData<List<SelectedComment>>().apply {
            value = testSelectedComment()
        }

    val selectedCommentListLiveData: MutableLiveData<List<SelectedComment>> =
        _selectedCommentListLiveData

    fun getSelectedComment() {
        _selectedCommentListLiveData.value = testSelectedComment()
    }

    private fun testSelectedComment(): List<SelectedComment> {
        val selectedCommentList = ArrayList<SelectedComment>()
        selectedCommentList.add(
            SelectedComment(
                0,
                "http://q1.qlogo.cn/g?b=qq&nk=1442198779&s=640",
                "zhansi",
                "huhu",
                1615392000000,
                "asdafjagladsfjadfs"
            )
        )
        selectedCommentList.add(
            SelectedComment(
                2,
                "http://q1.qlogo.cn/g?b=qq&nk=1442198779&s=640",
                "zhansi",
                null,
                1615392000000,
                "asdafjagladsfjadfs"
            )
        )
        return selectedCommentList
    }
}