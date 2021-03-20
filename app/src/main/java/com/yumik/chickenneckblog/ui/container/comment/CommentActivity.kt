package com.yumik.chickenneckblog.ui.container.comment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yumik.chickenneckblog.ProjectApplication
import com.yumik.chickenneckblog.R
import com.yumik.chickenneckblog.utils.OnLoadMoreListener
import com.yumik.chickenneckblog.utils.TipsUtil.showSnackbar
import com.yumik.chickenneckblog.utils.TipsUtil.showToast
import com.yumik.chickenneckblog.utils.setOnUnShakeClickListener

class CommentActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "CommentActivity"
    }

    private var articleId = -1
    private var commentId = -1
    private var listPage = 1
    private var totalPage = Int.MAX_VALUE

    private lateinit var viewModel: CommentViewModel
    private lateinit var adapter: CommentAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var commentEditText: EditText
    private lateinit var postTextView: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var parentCommentLayout: LinearLayout
    private lateinit var userPictureImageView: ImageView
    private lateinit var userNameTextView: TextView
    private lateinit var createTimeTextView: TextView
    private lateinit var commentTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        articleId = intent.getIntExtra("article_id", -1)
        commentId = intent.getIntExtra("comment_id", -1)

        viewModel = ViewModelProvider(this).get(CommentViewModel::class.java)
        recyclerView = findViewById(R.id.recyclerView)
        nestedScrollView = findViewById(R.id.nestedScrollView)
        commentEditText = findViewById(R.id.commentEditText)
        postTextView = findViewById(R.id.postTextView)
        toolbar = findViewById(R.id.toolbar)
        parentCommentLayout = findViewById(R.id.parentCommentLayout)
        userPictureImageView = findViewById(R.id.userPictureImageView)
        userNameTextView = findViewById(R.id.userNameTextView)
        createTimeTextView = findViewById(R.id.createTimeTextView)
        commentTextView = findViewById(R.id.commentTextView)

        initView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }
        toolbar.title = "查看评论"
        toolbar.setOnUnShakeClickListener {
            nestedScrollView.post {
                nestedScrollView.smoothScrollTo(0, 0)
            }
        }

        if (articleId == -1) {
            "无法读取评论".showToast(ProjectApplication.context)
            finish()
        }
        if (commentId == -1) {
            parentCommentLayout.visibility = View.GONE
        }
        viewModel.getComment(articleId, commentId, listPage)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CommentAdapter(this, articleId)
        recyclerView.adapter = adapter

        viewModel.commentListLiveData.observe(this, {
            val success = it.getOrNull()
            Log.d(TAG, success.toString())
            if (success != null) {
                if (success.data != null && success.code == 200) {
                    val data = success.data
                    listPage = data.page + 1
                    totalPage = data.totalPage
                    adapter.addAll(data.commentList)
                } else {
                    recyclerView.showSnackbar("${success.message}，错误代码：${success.code}")
                }
            } else {
                recyclerView.showSnackbar("网络请求失败，请检查网络连接！")
            }
        })

        recyclerView.addOnScrollListener(object : OnLoadMoreListener() {
            override fun onLoadMore() {
                if (listPage < totalPage) {
                    viewModel.getComment(articleId, commentId, listPage)
                }
            }
        })

        commentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length >= 8) {
                    postTextView.apply {
                        setTextColor(resources.getColor(R.color.enable_text, resources.newTheme()))
                        isEnabled = true
                        isClickable = true
                    }
                } else {
                    postTextView.apply {
                        setTextColor(resources.getColor(R.color.disable_text, resources.newTheme()))
                        isEnabled = false
                        isClickable = false
                    }
                }
            }
        })

        postTextView.setOnClickListener {

        }
    }
}