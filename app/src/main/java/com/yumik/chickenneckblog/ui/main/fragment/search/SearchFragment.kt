package com.yumik.chickenneckblog.ui.main.fragment.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.yumik.chickenneckblog.ProjectApplication
import com.yumik.chickenneckblog.R
import com.yumik.chickenneckblog.ui.main.MainActivity
import com.yumik.chickenneckblog.utils.SPUtil
import com.yumik.chickenneckblog.utils.SearchFunction.getClassify
import com.yumik.chickenneckblog.utils.SearchFunction.getSortOrder
import com.yumik.chickenneckblog.utils.SearchFunction.swapSortOrder
import com.yumik.chickenneckblog.utils.TipsUtil.showSnackbar

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()

        private const val TAG = "SearchFragment"
    }

    private var searchHistory by SPUtil(ProjectApplication.context, "search_history", "")

    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: SearchAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var filterChipGroup: ChipGroup
    private lateinit var sortOrderChip: Chip

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.search_fragment, container, false)
        recyclerView = root.findViewById(R.id.recyclerView)
        filterChipGroup = root.findViewById(R.id.filterChipGroup)
        sortOrderChip = root.findViewById(R.id.sortOrderChip)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        adapter = SearchAdapter(requireContext())
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        viewModel.searchArticleListLiveData.observe(viewLifecycleOwner) {
            val success = it.getOrNull()
            if (success != null) {
                if (success.data != null && success.code == 200) {
                    val data = success.data
                    adapter.reAddAll(data.articleList)
                } else {
                    recyclerView.showSnackbar("${success.message}，错误代码：${success.code}")
                }
            } else {
                recyclerView.showSnackbar("网络请求失败，请检查网络连接！")
            }
        }
        ProjectApplication.searchLiveData.observe(viewLifecycleOwner, {
            searchHistory = it
            viewModel.searchArticleList(
                it,
                getSearchItem(),
                getSortOrder(sortOrderChip.text as String).toString()
            )
        })
        sortOrderChip.setOnClickListener {
            sortOrderChip.text = swapSortOrder(sortOrderChip.text as String)
            val value = ProjectApplication.searchLiveData.value
            if (!value.isNullOrEmpty()) {
                viewModel.searchArticleList(
                    value,
                    getSearchItem(),
                    getSortOrder(sortOrderChip.text as String).toString()
                )
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (activity != null) {
            val mainActivity = activity as MainActivity
            mainActivity.closeSearchView()
        }
    }

    private fun getSearchItem(): String {
        var ret = 0
        for (item in filterChipGroup.checkedChipIds) {
            ret += getClassify(item)
        }
        return ret.toString()
    }
}