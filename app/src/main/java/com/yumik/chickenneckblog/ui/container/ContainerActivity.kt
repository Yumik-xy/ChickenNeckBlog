package com.yumik.chickenneckblog.ui.container

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.appbar.AppBarLayout
import com.yumik.chickenneckblog.ProjectApplication
import com.yumik.chickenneckblog.R
import com.yumik.chickenneckblog.ui.BaseActivity
import com.yumik.chickenneckblog.ui.container.comment.CommentActivity
import com.yumik.chickenneckblog.utils.LongNumberFormat.format
import com.yumik.chickenneckblog.utils.MarkdownEscape.escape
import com.yumik.chickenneckblog.utils.TipsUtil.showSnackbar
import com.yumik.chickenneckblog.utils.TipsUtil.showToast
import com.yumik.chickenneckblog.utils.formatTime
import com.yumik.chickenneckblog.utils.setOnUnShakeClickListener


class ContainerActivity : BaseActivity() {

    companion object {
        private const val TAG = "ContainerActivity"
    }

    private lateinit var viewModel: ContainerViewModel
    private lateinit var adapter: ContainerAdapter

    private lateinit var recyclerView: RecyclerView

    private lateinit var toolbar: Toolbar
    private lateinit var appBar: AppBarLayout
    private lateinit var toolbarContainer: LinearLayout
    private lateinit var authorPictureImageView: ImageView
    private lateinit var authorTextView: TextView
    private lateinit var followTextView: TextView
    private lateinit var authorIntroductionTextView: TextView
    private lateinit var favouriteImageView: ImageView
    private lateinit var disFavouriteImageView: ImageView
    private lateinit var favouriteTextView: TextView
    private lateinit var markImageView: ImageView
    private lateinit var unMarkImageView: ImageView
    private lateinit var markTextView: TextView
    private lateinit var commentTextView: TextView
    private lateinit var favoriteLayout: LinearLayout
    private lateinit var markLinearLayout: LinearLayout
    private lateinit var commentLinearLayout: LinearLayout

    private lateinit var webView: WebView
    var markdown = ""

    private lateinit var nestedScrollView: NestedScrollView

    private var articleId = -1
    private var authorId = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

//        AndroidBug5497Workaround.assistActivity(this) //全屏模式下使用
//        android:windowSoftInputMode="adjustResize" //非全屏模式下使用 包含webView情况下
        viewModel = ViewModelProvider(this).get(ContainerViewModel::class.java)
        recyclerView = findViewById(R.id.recyclerView)
        toolbar = findViewById(R.id.toolbar)
        appBar = findViewById(R.id.appBar)
        toolbarContainer = findViewById(R.id.toolbarContainer)
        webView = findViewById(R.id.webView)
        nestedScrollView = findViewById(R.id.nestedScrollView)

        authorPictureImageView = findViewById(R.id.authorPictureImageView)
        authorTextView = findViewById(R.id.authorTextView)
        followTextView = findViewById(R.id.followTextView)
        authorIntroductionTextView = findViewById(R.id.authorIntroductionTextView)
        favouriteImageView = findViewById(R.id.favouriteImageView)
        disFavouriteImageView = findViewById(R.id.disFavouriteImageView)
        favouriteTextView = findViewById(R.id.favouriteTextView)
        markImageView = findViewById(R.id.markImageView)
        unMarkImageView = findViewById(R.id.unMarkImageView)
        markTextView = findViewById(R.id.markTextView)
        commentTextView = findViewById(R.id.commentTextView)
        favoriteLayout = findViewById(R.id.favoriteLayout)
        markLinearLayout = findViewById(R.id.markLinearLayout)
        commentLinearLayout = findViewById(R.id.commentLinearLayout)


        articleId = intent.getIntExtra("article_id", 0)
        if (articleId == -1) {
            "该文章无法访问，请检查网络或联系管理！".showToast(ProjectApplication.context)
            finish()
        }

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

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }
        toolbar.title = "title"
        toolbar.setOnUnShakeClickListener {
            nestedScrollView.post {
                nestedScrollView.smoothScrollTo(0, 0)
            }
        }


        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.textZoom = 100
        webView.loadUrl("file:///android_asset/template.html")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                webView.evaluateJavascript("setMarkdown(\"" + markdown.escape() + "\")") {}
            }
        }
//        webView.setBackgroundColor(0)

        adapter = ContainerAdapter(this) {
            val intent = Intent(this, CommentActivity::class.java)
            intent.putExtra("article_id", articleId)
            startActivity(intent)
        }
        recyclerView.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        recyclerView.adapter = adapter

        viewModel.getArticle(articleId)
        viewModel.articleListLiveData.observe(this, {
            val success = it.getOrNull()
            if (success != null) {
                if (success.data != null && success.code == 200) {
                    val data = success.data
                    markdown = data.content
                    webView.evaluateJavascript("setMarkdown(\"" + markdown.escape() + "\")") {}
                    Glide.with(authorPictureImageView.context)
                        .load(data.user.picture)
                        .placeholder(R.drawable.ic_drawer_user)
                        .error(R.drawable.ic_image_error)
                        .thumbnail(0.1f)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .transform(CenterInside(), CircleCrop())
                        .into(authorPictureImageView)
                    toolbar.title = data.title
                    toolbar.subtitle = data.time.formatTime()
                    authorId = data.user.id
                    authorTextView.text = data.user.name
                    authorIntroductionTextView.text = data.user.introduction
                    markTextView.text = data.markedNumber.format()
                    if (data.marked) {
                        markImageView.visibility = View.VISIBLE
                        unMarkImageView.visibility = View.GONE
                    } else {
                        markImageView.visibility = View.GONE
                        unMarkImageView.visibility = View.VISIBLE
                    }
                    favouriteTextView.text = data.favoriteNumber.format()
                    if (data.marked) {
                        favouriteImageView.visibility = View.VISIBLE
                        disFavouriteImageView.visibility = View.GONE
                    } else {
                        favouriteImageView.visibility = View.GONE
                        disFavouriteImageView.visibility = View.VISIBLE
                    }
                    commentTextView.text = data.commentNumber.format()
                    if (data.commentList != null) {
                        adapter.addAll(data.commentList)
                    }
                    followTextView.text = if (data.followed) "已关注" else "+关注"
                } else {
                    recyclerView.showSnackbar("${success.message}，错误代码：${success.code}")
                }
            } else {
                recyclerView.showSnackbar("网络请求失败，请检查网络连接！")
            }
        })

        favoriteLayout.setOnUnShakeClickListener {
            viewModel.setFavourite(articleId)
        }

        markLinearLayout.setOnUnShakeClickListener {
            viewModel.setMark(articleId)
        }

        commentLinearLayout.setOnUnShakeClickListener {
            nestedScrollView.post {
                nestedScrollView.smoothScrollTo(0, recyclerView.top)
            }
        }

        followTextView.setOnUnShakeClickListener {
            if (authorId != -1) {
                viewModel.setFollow(authorId)
            } else {
                recyclerView.showSnackbar("未获取到用户信息")
            }
        }
    }
}
