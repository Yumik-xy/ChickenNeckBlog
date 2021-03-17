package com.yumik.chickenneckblog.ui.main.fragment.follow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yumik.chickenneckblog.logic.model.Article

class FollowModel : ViewModel() {
    private val _articleListLiveData = MutableLiveData<List<Article>>().apply {
        value = testArticleList()
    }
    val articleListLiveData: MutableLiveData<List<Article>> = _articleListLiveData
    val articleList = ArrayList<Article>()


    private fun testArticleList() =
        listOf(
            Article(
                0,
                "现代经济基础和上层建筑决定了我到底能不能学好数理化",
                "现代经济基础和上层建筑决定了我到底能不能学好数理化现代经济基础和上层建筑决定了我到底能不能学好数理化",
                1615392000000,
                20,
                true,
                1000,
                "http://q1.qlogo.cn/g?b=qq&nk=1442198779&s=640",
                "大基佬",
                "http://q1.qlogo.cn/g?b=qq&nk=1442198779&s=640"
            ),
            Article(
                0,
                "现代经济基础和上层建筑决定了我到底能不能学好数理化",
                "现代经济基础和上层建筑决定了我到底能不能学好数理化现代经济基础和上层建筑决定了我到底能不能学好数理化",
                1615392000000,
                20,
                true,
                1000,
                "http://q1.qlogo.cn/g?b=qq&nk=1442198779&s=640",
                "大基佬",
                "http://q1.qlogo.cn/g?b=qq&nk=1442198779&s=640"
            ),
            Article(
                0,
                "现代经济基础和上层建筑决定了我到底能不能学好数理化",
                "现代经济基础和上层建筑决定了我到底能不能学好数理化现代经济基础和上层建筑决定了我到底能不能学好数理化",
                1615392000000,
                20,
                true,
                1000,
                null,
                "大基佬",
                "http://q1.qlogo.cn/g?b=qq&nk=1442198779&s=640"
            ),
            Article(
                0,
                "现代经济基础和上层建筑决定了我到底能不能学好数理化",
                "现代经济基础和上层建筑决定了我到底能不能学好数理化现代经济基础和上层建筑决定了我到底能不能学好数理化",
                1615392000000,
                20,
                true,
                1000,
                "http://q1.qlogo.cn/g?b=qq&nk=1442198779&s=640",
                "大基佬",
                null
            ),
            Article(
                0,
                "现代经济基础和上层建筑决定了我到底能不能学好数理化",
                "现代经济基础和上层建筑决定了我到底能不能学好数理化现代经济基础和上层建筑决定了我到底能不能学好数理化现代经济基础和上层建筑决定了我到底能不能学好数理化现代经济基础和上层建筑决定了我到底能不能学好数理化",
                1615392000000,
                20,
                true, 1000,
                "http://q1.qlogo.cn/g?b=qq&nk=1442198779&s=640",
                "大基佬",
                "http://q1.qlogo.cn/g?b=qq&nk=1442198779&s=640"
            ),
        )
}