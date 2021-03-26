package com.yumik.chickenneckblog.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.yumik.chickenneckblog.ProjectApplication
import com.yumik.chickenneckblog.R
import com.yumik.chickenneckblog.ui.BaseActivity
import com.yumik.chickenneckblog.ui.login.LoginActivity
import com.yumik.chickenneckblog.ui.main.service.DownloadService
import com.yumik.chickenneckblog.utils.GetDirection.getDiskCacheDir
import com.yumik.chickenneckblog.utils.SPUtil
import com.yumik.chickenneckblog.utils.TipsUtil.showSnackbar
import com.yumik.chickenneckblog.utils.setOnUnShakeClickListener


class MainActivity : BaseActivity() {

    companion object {
        private const val TAG = "MainActivity"
        lateinit var viewModel: MainViewModel
    }

    private var token by SPUtil(this, "token", "")

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var navController: NavController
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var navHeadView: View
    private lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        loginTry()
        initView()
        checkUpload()
    }

    private fun checkUpload() {
        viewModel.checkUpdateLiveData.observe(this, {
            val success = it.getOrNull()
            if (success != null) {
                if (success.data != null && success.code == 200) {
                    val data = success.data
                    Log.d(TAG, data.toString())
                    val packageManager = packageManager
                    val packageInfo = packageManager.getPackageInfo(packageName, 0)
                    val versionCode =
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                            packageInfo.longVersionCode
                        } else {
                            packageInfo.versionCode.toLong()
                        }
                    if (versionCode < data.versionCode) {
                        Log.d(TAG, "Try to update")
                        val intent = Intent(this, DownloadService::class.java)
                        intent.putExtra("url", data.downLoadUrl)
                        intent.putExtra("path", getDiskCacheDir(this) + data.name + ".apk")
                        intent.putExtra("md5", data.md5)
                        intent.putExtra("important", data.important)

                        startService(intent)
                    }
                }
            }
        })
//        val url = "file/download"
//        val path = getDiskCacheDir(this) + "1.apk"
    }

    private fun loginTry() {
        if (token == "") {
            switchFragment(R.id.nav_follow)
            return
        }
        ProjectApplication.token = token
        viewModel.loginUser(ProjectApplication.token)
        viewModel.tokenLoginBeanListLiveData.observe(this, {
            val success = it.getOrNull()
            if (success != null) {
                if (success.data != null && success.code == 200 && success.data.token.isNotEmpty()) {
                    val data = success.data
                    token = data.token
                    ProjectApplication.token = token
                    ProjectApplication.loginStateLiveData.value = data
                } else {
                    toolbar.showSnackbar("自动登陆失败，错误代码：${success.code}")
                    logout()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        searchViewInit(menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_selected,
                R.id.nav_follow,
                R.id.nav_user,
                R.id.nav_settings,
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navHeadView = navView.inflateHeaderView(R.layout.nav_header_main)
        navHeadView.findViewById<ImageView>(R.id.avatarImageView).setOnUnShakeClickListener {
            if (ProjectApplication.token == "") {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("extra_msg", "现在登录，纵享丝滑！")
                startActivity(intent)
            } else {
                switchFragment(R.id.nav_user)
            }
            drawerLayout.closeDrawers()
        }

        ProjectApplication.loginStateLiveData.observe(this, {
            toolbar.showSnackbar("登录成功")
            navHeadView.apply {
                val avatarImageView = findViewById<ImageView>(R.id.avatarImageView)
                if (it.user.picture != null) {
                    Glide.with(avatarImageView)
                        .load(it.user.picture)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .transform(CenterInside(), CircleCrop())
                        .into(avatarImageView)
                }
                val userTextView = findViewById<TextView>(R.id.userTextView)
                userTextView.text = it.user.name
                val uidTextView = findViewById<TextView>(R.id.uidTextView)
                uidTextView.text = it.user.id.toString()
            }
        })
    }

    // drawerItem 点击事件
    fun drawerItemOnClicked(item: MenuItem) {
        drawerLayout.closeDrawers()
        when (item.itemId) {
            R.id.nav_sign_out -> {
                MaterialAlertDialogBuilder(this)
                    // resources.getString()
                    .setTitle("警告")
                    .setMessage("是否退出登录")
                    .setNegativeButton("退出") { _, _ ->
                        logout()
                    }
                    .setPositiveButton("取消") { _, _ -> }
                    .show()
            }
        }
    }

    private fun searchViewInit(menu: Menu) {
        val item = menu.findItem(R.id.action_search)
        searchView = item.actionView as SearchView
        searchView.apply {
//            setIconifiedByDefault(false)
            queryHint = "输入你要搜索的文章"
            maxWidth = Integer.MAX_VALUE
//            失去焦点关闭
            setOnQueryTextFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    switchFragment(R.id.nav_search)
                } else {
                    closeSearchView()
                }
            }
//            显示搜索按钮
            isSubmitButtonEnabled = true
            isQueryRefinementEnabled = true
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrEmpty()) {
                        ProjectApplication.searchLiveData.value = query
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    fun closeSearchView() {
        if (::searchView.isInitialized) {
            searchView.clearFocus()
            searchView.onActionViewCollapsed()
        }
    }

    /**
     * Fragment跳转
     */
    private fun switchFragment(fragmentId: Int) {
        try {
            if (navController.currentDestination?.id != fragmentId) {
                navController.navigate(fragmentId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun logout() {
        token = ""
        ProjectApplication.token = ""
        navHeadView.apply {
            val avatarImageView = findViewById<ImageView>(R.id.avatarImageView)
            Glide.with(avatarImageView)
                .load(R.drawable.ic_logout)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(CenterInside(), CircleCrop())
                .into(avatarImageView)
            val userTextView = findViewById<TextView>(R.id.userTextView)
            userTextView.text = "尚未登陆"
            val uidTextView = findViewById<TextView>(R.id.uidTextView)
            uidTextView.text = "10001"
        }
    }
}