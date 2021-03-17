package com.yumik.chickenneckblog.ui.container

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.yumik.chickenneckblog.R
import com.yumik.chickenneckblog.ui.container.comment.CommentActivity
import com.yumik.chickenneckblog.utils.MarkdownEscape.escape
import kotlin.properties.Delegates

class ContainerActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ContainerActivity"
    }

    private lateinit var viewModel: ContainerViewModel
    private lateinit var adapter: ContainerAdapter

    private lateinit var recyclerView: RecyclerView

    private lateinit var toolbar: Toolbar
    private lateinit var toolbarLayout: CollapsingToolbarLayout

    private lateinit var webView: WebView
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var postTextView: TextView
    private lateinit var commentEditText: EditText

    private var articleId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

//        AndroidBug5497Workaround.assistActivity(this) //全屏模式下使用
//        android:windowSoftInputMode="adjustResize" //非全屏模式下使用 包含webView情况下
        viewModel = ViewModelProvider(this).get(ContainerViewModel::class.java)
        recyclerView = findViewById(R.id.recyclerView)

        toolbar = findViewById(R.id.toolbar)
        toolbarLayout = findViewById(R.id.toolbar_layout)
//        fab = findViewById(R.id.fab)
        webView = findViewById(R.id.webView)
        nestedScrollView = findViewById(R.id.nestedScrollView)
        postTextView = findViewById(R.id.postTextView)
        commentEditText = findViewById(R.id.commentEditText)

        articleId = intent.getIntExtra("article_id", 0)

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
        toolbarLayout.title = ""
        toolbar.setOnClickListener {
            nestedScrollView.smoothScrollTo(0, 0)
        }


        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.textZoom = 100
        webView.loadUrl("file:///android_asset/template.html")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                val markdown = "# sduContentionCourse\n" +
                        "\n" +
                        "## 山东大学选课\n" +
                        "\n" +
                        "### 使用方法\n" +
                        "\n" +
                        "1. 在chrome进入本科生选课系统登录后，按F12点击console\n" +
                        "\n" +
                        "2. 打开代码1.js 前两行的两个数组表示存放抢课的课程号课序号，也是唯一一个需要修改的地方\n" +
                        "\n" +
                        "   例如：\n" +
                        "\n" +
                        "   ```js\n" +
                        "   var kch = ['sd07517110', 'sd07510180'];\n" +
                        "   var kxh = ['600', '600'];\n" +
                        "   ```\n" +
                        "\n" +
                        "   表示需要抢课程号sd07517110课序号600的课 以及 课程号sd07510180课序号600的课  \n" +
                        "\n" +
                        "3. 将修改后的代码粘贴到chrome的console里面，回车执行 \n" +
                        "4. 由于存在教务老师突然添加或者删除课程的情况，每次运行需要**约一分钟的初始化**来读取课程列表，读取完成后会在命令行输出\"读取完成\"并自动开始抢课。\n" +
                        "5. **终止方法**：F5刷新即可\n" +
                        "\n" +
                        "p.s. 默认抢课速度300ms/次\n" +
                        "\n" +
                        "waring:为了人类之间的和平，为了不整垮学校服务器，请不要把频率调的太高！\n" +
                        "\n" +
                        "如果发现抢的课不对，一般为教务老师突然添加或删除课程导致课程列表发生变化，请刷新页面-退课-重新执行脚本。\n" +
                        "\n" +
                        "由于调试时Chrome会缓存所有的请求, 会造成Chrome占用大量的内存, 解决方案是NetWork-> Ctrl+E\n" +
                        "\n" +
                        "## 山东大学自动评价\n" +
                        "\n" +
                        "### 使用方法\n" +
                        "\n" +
                        "1. 在chrome进入本科生选课系统登录后，按F12点击console\n" +
                        "\n" +
                        "2. 打开代码2.js, 复制代码 , 粘贴到chrome的console里面，回车执行 \n" +
                        "\n" +
                        "3. 2s执行一条\n"
                webView.evaluateJavascript("setMarkdown(\"" + markdown.escape() + "\")") {
                }
            }
        }

//        postTextView.setOnClickListener {
//
//        }

//        commentEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//            override fun afterTextChanged(s: Editable?) {
//                if (s != null && s.length >= 8) {
//                    postTextView.apply {
//                        setTextColor(resources.getColor(R.color.enable_text, resources.newTheme()))
//                        isEnabled = true
//                        isClickable = true
//                    }
//                } else {
//                    postTextView.apply {
//                        setTextColor(resources.getColor(R.color.disable_text, resources.newTheme()))
//                        isEnabled = false
//                        isClickable = false
//                    }
//                }
//            }
//        })

        adapter = ContainerAdapter(this) {
            print("recyclerView")
            val intent = Intent(this, CommentActivity::class.java)
            intent.putExtra("article_id", articleId)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        viewModel.selectedCommentListLiveData.observe(this, {
            Log.d(TAG, it.toString())
            adapter.addAll(it)
        })

    }
}
