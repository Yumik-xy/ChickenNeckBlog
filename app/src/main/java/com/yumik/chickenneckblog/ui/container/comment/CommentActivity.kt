package com.yumik.chickenneckblog.ui.container.comment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.yumik.chickenneckblog.ProjectApplication
import com.yumik.chickenneckblog.R
import com.yumik.chickenneckblog.utils.TipsUtil.showToast
import kotlin.properties.Delegates

class CommentActivity : AppCompatActivity() {

    private var articleId by Delegates.notNull<Int>()
    private var commentId by Delegates.notNull<Int>()

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

        if (articleId == -1) {
            "无法读取文章".showToast(ProjectApplication.context)
            finish()
        }

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

    private fun initView() {
        /**
         * 判断是否需要显示头部ParentComment信息
         */
        if (commentId == -1) {
            parentCommentLayout.visibility = View.GONE
        }
        /**
         * 输入字数大于8个字才可以发表
         */
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