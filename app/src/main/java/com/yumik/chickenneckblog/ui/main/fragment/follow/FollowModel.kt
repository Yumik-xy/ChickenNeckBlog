package com.yumik.chickenneckblog.ui.main.fragment.follow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yumik.chickenneckblog.logic.model.ArticleItem

class FollowModel : ViewModel() {
    private val _articleListLiveData = MutableLiveData<List<ArticleItem>>().apply {
        value = testArticleList()
    }
    val articleItemListLiveData: MutableLiveData<List<ArticleItem>> = _articleListLiveData
    val articleList = ArrayList<ArticleItem>()


    private fun testArticleList() =
        listOf(
            ArticleItem(
                0,
                "现代经济基础和上层建筑决定了我到底能不能学好数理化",
                "现代经济基础和上层建筑决定了我到底能不能学好数理化现代经济基础和上层建筑决定了我到底能不能学好数理化",
                1615392000000,
                20L,
                true,
                2000L,
                "http://q1.qlogo.cn/g?b=qq&nk=1442198779&s=640",
                "大基佬",
                "http://q1.qlogo.cn/g?b=qq&nk=1442198779&s=640"
            ),
            ArticleItem(
                0,
                "现代经济基础和上层建筑决定了我到底能不能学好数理化",
                "现代经济基础和上层建筑决定了我到底能不能学好数理化现代经济基础和上层建筑决定了我到底能不能学好数理化",
                1615392000000,
                20L,
                true,
                2000L,
                "http://q1.qlogo.cn/g?b=qq&nk=1442198779&s=640",
                "大基佬",
                "http://q1.qlogo.cn/g?b=qq&nk=1442198779&s=640"
            ),ArticleItem(
                0,
                "现代经济基础和上层建筑决定了我到底能不能学好数理化",
                "现代经济基础和上层建筑决定了我到底能不能学好数理化现代经济基础和上层建筑决定了我到底能不能学好数理化",
                1615392000000,
                20L,
                true,
                2000L,
                "http://q1.qlogo.cn/g?b=qq&nk=1442198779&s=640",
                "大基佬",
                "http://q1.qlogo.cn/g?b=qq&nk=1442198779&s=640"
            ),
        )
}