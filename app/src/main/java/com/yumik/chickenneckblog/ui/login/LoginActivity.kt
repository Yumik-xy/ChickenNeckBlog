package com.yumik.chickenneckblog.ui.login

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.yumik.chickenneckblog.ProjectApplication
import com.yumik.chickenneckblog.R
import com.yumik.chickenneckblog.ui.main.MainActivity
import com.yumik.chickenneckblog.utils.GetMd5Util.getMD5
import com.yumik.chickenneckblog.utils.ImmersiveUtil.immersive
import com.yumik.chickenneckblog.utils.SPUtil
import com.yumik.chickenneckblog.utils.TipsUtil.showSnackbar

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
    }

    var token by SPUtil(this, "token", "")
    var user by SPUtil(this, "user", "")

    private lateinit var viewModel: LoginViewModel

    private var checked = false

    private lateinit var loginButton: Button
    private lateinit var extraTextView: TextView
    private lateinit var userLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var userEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var checkAgreementTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginButton = findViewById(R.id.loginButton)
        extraTextView = findViewById(R.id.extraTextView)
        userLayout = findViewById(R.id.userLayout)
        passwordLayout = findViewById(R.id.passwordInputLayout)
        userEditText = findViewById(R.id.userEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        checkAgreementTextView = findViewById(R.id.checkAgreementTextView)

        initView()
        immersive(this)
        createSpannableString()
    }

    private fun initView() {
        if (user != "") {
            userEditText.setText(user)
            passwordEditText.requestFocus()
        }
        loginButton.setOnClickListener { view ->
            if (!checked) {
                ObjectAnimator.ofFloat(
                    findViewById<TextView>(R.id.checkAgreementTextView), "translationX",
                    0f, 10f, -10f, 10f, -10f, 5f, -5f, 2f, -2f, 0f
                ).also {
                    it.duration = 1000
                }.start()
            } else if (userEditText.text.isNullOrEmpty() || passwordEditText.text.isNullOrEmpty()) {
                view.showSnackbar("用户名或密码不能为空！")
            } else {
                viewModel.loginUser(
                    userEditText.text.toString(),
                    passwordEditText.text.toString().getMD5()
                )
            }
        }
        extraTextView.text = intent.getStringExtra("extra_msg")

        viewModel.userLoginBeanListLiveData.observe(this, {
            val success = it.getOrNull()
            if (success != null) {
                if (success.data != null && success.code == 200) {
                    val data = success.data
                    user = userEditText.text.toString()
                    token = data.token
                    ProjectApplication.token = token
                    ProjectApplication.loginStateLiveData.value = data
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    loginButton.showSnackbar("${success.message}，错误代码：${success.code}")
                }
            }
        })
    }

    private fun createSpannableString() {
        SpannableString("  同意《用户协议》《隐私协议》《菜鸡协议》").also {
            it.setSpan(
                ForegroundColorSpan(Color.parseColor("#CDCDCD")),
                2,
                4,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            it.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    Toast.makeText(this@LoginActivity, "点击用户协议", Toast.LENGTH_SHORT).show()
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = Color.WHITE
                    ds.isUnderlineText = false
                }
            }, 4, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            it.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    Toast.makeText(this@LoginActivity, "点击隐私协议", Toast.LENGTH_SHORT).show()
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = Color.WHITE
                    ds.isUnderlineText = false
                }
            }, 10, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            it.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    Toast.makeText(this@LoginActivity, "点击用菜鸡协议", Toast.LENGTH_SHORT).show()
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = Color.WHITE
                    ds.isUnderlineText = false
                }
            }, 16, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            checkAgreementTextView.apply {
                text = it
                movementMethod = LinkMovementMethod.getInstance()
                highlightColor = Color.TRANSPARENT
                setOnClickListener {
                    if (selectionStart == -1 && selectionEnd == -1) {
                        checked = !checked
                        if (checked) {
                            setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(
                                    this@LoginActivity,
                                    R.drawable.ic_check_on
                                ),
                                null,
                                null,
                                null
                            )
                        } else {
                            setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(
                                    this@LoginActivity,
                                    R.drawable.ic_check_off
                                ),
                                null,
                                null,
                                null
                            )
                        }
                    }
                }
            }
        }
    }
}