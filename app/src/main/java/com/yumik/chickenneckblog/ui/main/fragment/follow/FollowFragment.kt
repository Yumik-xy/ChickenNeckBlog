package com.yumik.chickenneckblog.ui.main.fragment.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.yumik.chickenneckblog.R
import com.yumik.chickenneckblog.ui.main.fragment.adapter.ArticleItemAdapter
import com.yumik.chickenneckblog.utils.TipsUtil.showSnackbar


class FollowFragment : Fragment() {

    companion object {
        fun newInstance() = FollowFragment()
    }

    private lateinit var viewModel: FollowModel
    private lateinit var adapter: FollowAdapter


    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.follow_fragment, container, false)
        fab = root.findViewById(R.id.fab)
        recyclerView = root.findViewById(R.id.recyclerView)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FollowModel::class.java)
        initView()
    }

    private fun initView() {
        fab.setOnClickListener { view ->
            view.showSnackbar("Replace with your own action", "Done", Snackbar.LENGTH_LONG)
        }
        //        绑定recyclerView的adapter
        adapter = FollowAdapter(viewModel.articleList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        viewModel.articleItemListLiveData.observe(viewLifecycleOwner, {
            viewModel.articleList.clear()
            viewModel.articleList.addAll(it)
        })
    }
}

//    private fun replaceFragment(fragment: Fragment) {
//        val fragmentManager = childFragmentManager
//        val transaction = fragmentManager.beginTransaction()
//        transaction.replace(R.id.articleFragment, fragment)
//        transaction.commit()
//    }

