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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.yumik.chickenneckblog.ProjectApplication
import com.yumik.chickenneckblog.R
import com.yumik.chickenneckblog.logic.model.Comment
import com.yumik.chickenneckblog.ui.BaseActivity
import com.yumik.chickenneckblog.utils.OnLoadMoreListener
import com.yumik.chickenneckblog.utils.TipsUtil.showSnackbar
import com.yumik.chickenneckblog.utils.TipsUtil.showToast
import com.yumik.chickenneckblog.utils.formatTime
import com.yumik.chickenneckblog.utils.setOnUnShakeClickListener

class CommentActivity : BaseActivity() {

    companion object {
        private const val TAG = "CommentActivity"
    }

    private var articleId = 0
    private var data: Comment? = null
    private var listPage = 1
    private var totalPage = Int.MAX_VALUE
    private var replyUser: Int = 0

    private lateinit var viewModel: CommentViewModel
    private lateinit var adapter: CommentAdapter

    private lateinit var recyclerView: RecyclerView

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

        articleId = intent.getIntExtra("article_id", 0)
        data = intent.getSerializableExtra("data") as? Comment

        viewModel = ViewModelProvider(this).get(CommentViewModel::class.java)
        recyclerView = findViewById(R.id.recyclerView)
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

    override fun onBackPressed() {
        if (replyUser != data?.id ?: 0 && commentEditText.text.isEmpty()) {
            commentEditText.hint = "说点什么吧..."
            replyUser = data?.id ?: 0
        } else {
            super.onBackPressed()
        }
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
            recyclerView.post {
                recyclerView.smoothScrollToPosition(0)
            }
        }

        if (articleId == 0) {
            "无法读取评论".showToast(ProjectApplication.context)
            finish()
        }
        if (data == null) {
            parentCommentLayout.visibility = View.GONE
        } else {
            val userPictureImageView: ImageView = findViewById(R.id.userPictureImageView)
            val userNameTextView: TextView = findViewById(R.id.userNameTextView)
            val createTimeTextView: TextView = findViewById(R.id.createTimeTextView)
            val commentTextView: TextView = findViewById(R.id.commentTextView)
            data?.apply {
                userNameTextView.text = user.name
                commentTextView.text = content
                createTimeTextView.text = time.formatTime()
                if (user.picture.isNullOrEmpty()) {
                    userPictureImageView.visibility = View.GONE
                } else {
                    userPictureImageView.visibility = View.VISIBLE
                    Glide.with(userPictureImageView.context)
                        .load(user.picture)
                        .placeholder(R.drawable.ic_drawer_user)
                        .error(R.drawable.ic_image_error)
                        .thumbnail(0.1f)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .transform(CenterInside(), CircleCrop())
                        .into(userPictureImageView)
                }
            }
        }
        viewModel.getComment(articleId, data?.id ?: 0, listPage)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CommentAdapter(this, articleId)
        adapter.setOnCommentClickListener(object : CommentAdapter.OnCommentClick {
            override fun onCommentClick(item: Comment) {
                commentEditText.hint = "@${item.user.name}"
                replyUser = item.id
                commentEditText.requestFocus()
            }
        })
        adapter.setOnAgreeClickListener(object : CommentAdapter.OnAgreeClick {
            override fun onAgreeClick(item: Comment, agree: Int) {
                viewModel.postAgreeOrNotLiveData(articleId, item.id, agree)
            }

        })
        recyclerView.adapter = adapter

        viewModel.getComment(articleId, data?.id ?: 0, listPage)

        recyclerView.addOnScrollListener(object : OnLoadMoreListener() {

            override fun onLoadMore() {
                Log.d("TAG", "addOnScrollListener")
                if (listPage <= totalPage) {
                    viewModel.getComment(articleId, data?.id ?: 0, listPage)
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
            viewModel.postComment(articleId, replyUser, commentEditText.text.toString())
            postTextView.apply {
                setTextColor(resources.getColor(R.color.disable_text, resources.newTheme()))
                isEnabled = false
                isClickable = false
            }
        }



        viewModel.commentListLiveData.observe(this, {
            val success = it.getOrNull()
            Log.d(TAG, success.toString())
            if (success != null) {
                if (success.data != null && success.code == 200) {
                    val data = success.data
                    listPage = data.page + 1
                    totalPage = data.totalPage
                    if (data.page == 1) {
                        adapter.reAddAll(data.commentList)
                    } else {
                        adapter.addAll(data.commentList)
                    }
                } else if (success.code == 30000 || success.code == 30001) {
                    // 没有更多文章了，不需要显示
                } else {
                    recyclerView.showSnackbar("${success.message}，错误代码：${success.code}")
                }
            } else {
                recyclerView.showSnackbar("网络请求失败，请检查网络连接！")
            }
        })

        viewModel.postCommentLiveData.observe(this, {
            val success = it.getOrNull()
            Log.d(TAG, success.toString())
            if (success != null) {
                if (success.data != null && success.code == 200) {
                    val data = success.data
                    if (this.data != null || data.commentId == 0) {
                        adapter.add(data.comment)
                    } else {
                        adapter.add(data.comment, data.commentId)
                    }
                    commentEditText.text.clear()
                } else {
                    recyclerView.showSnackbar("${success.message}，错误代码：${success.code}")
                }
            } else {
                recyclerView.showSnackbar("网络请求失败，请检查网络连接！")
            }
        })

        viewModel.postAgreeOrNotLiveData.observe(this, {
            val success = it.getOrNull()
            Log.d(TAG, success.toString())
            if (success != null) {
                if (success.data != null && success.code == 200) {
                    val data = success.data
                    adapter.add(data)
                } else {
                    recyclerView.showSnackbar("${success.message}，错误代码：${success.code}")
                }
            } else {
                recyclerView.showSnackbar("网络请求失败，请检查网络连接！")
            }
        })
    }
}