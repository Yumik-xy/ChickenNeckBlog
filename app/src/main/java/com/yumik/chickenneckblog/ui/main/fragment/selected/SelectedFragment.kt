package com.yumik.chickenneckblog.ui.main.fragment.selected

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.yumik.chickenneckblog.ProjectApplication
import com.yumik.chickenneckblog.R
import com.yumik.chickenneckblog.ui.main.fragment.adapter.ArticleItemAdapter
import com.yumik.chickenneckblog.utils.OnLoadMoreListener
import com.yumik.chickenneckblog.utils.TipsUtil.showSnackbar
import java.util.*

class SelectedFragment : Fragment() {

    companion object {
        fun newInstance() = SelectedFragment()
        private const val TAG = "SelectedFragment"
    }

    private var listPage = 1
    private var totalPage = Int.MAX_VALUE

    private lateinit var viewModel: SelectedViewModel
    private lateinit var adapter: ArticleItemAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar

    private var firstClickTime = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.selected_fragment, container, false)
        fab = root.findViewById(R.id.fab)
        recyclerView = root.findViewById(R.id.recyclerView)
        swipeRefresh = root.findViewById(R.id.swipeRefresh)
        progressBar = root.findViewById(R.id.progressBar)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SelectedViewModel::class.java)
        initView()
    }

    private fun initView() {
        fab.setOnClickListener { view ->
            view.showSnackbar("Replace with your own action", "Done", Snackbar.LENGTH_LONG)
        }
        //        绑定recyclerView的adapter
        adapter = ArticleItemAdapter(requireContext())
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter


        swipeRefresh.setColorSchemeResources(R.color.secondary)
        viewModel.selectedArticleListLiveData.observe(viewLifecycleOwner, {
            val success = it.getOrNull()
            if (success != null) {
                if (success.data != null && success.code == 200) {
                    val data = success.data
                    listPage = data.page + 1
                    totalPage = data.totalPage
                    adapter.addAll(data.articleList)
                } else {
                    fab.showSnackbar("${success.message}，错误代码：${success.code}")
                }
            } else {
                fab.showSnackbar("网络请求失败，请检查网络连接！")
            }
            swipeRefresh.isRefreshing = false
            progressBar.visibility = View.GONE
        })
        swipeRefresh.setOnRefreshListener {
            refreshList()
        }
        recyclerView.addOnScrollListener(object : OnLoadMoreListener() {
            override fun onLoadMore() {
                progressBar.visibility = View.VISIBLE
                viewModel.getSelectedArticleList(listPage)
            }
        })
        activity?.findViewById<Toolbar>(R.id.toolbar)?.apply {
            setOnClickListener {
                if (Date().time - firstClickTime <= 400) {
                    // Double Click
                    firstClickTime = 0
                    swipeRefresh.isRefreshing = true
                    refreshList()
                } else {
                    firstClickTime = Date().time
                    recyclerView.smoothScrollToPosition(0)
                }
            }
        }

        ProjectApplication.loginStateLiveData.observe(viewLifecycleOwner, {
            refreshList()
        })
    }

    private fun refreshList() {
        listPage = 1
        viewModel.getSelectedArticleList(listPage)
    }
}