package com.yumik.chickenneckblog.ui.main.fragment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.yumik.chickenneckblog.R
import com.yumik.chickenneckblog.logic.model.ArticleItem
import com.yumik.chickenneckblog.ui.container.ContainerActivity
import com.yumik.chickenneckblog.utils.LongNumberFormat.format
import com.yumik.chickenneckblog.utils.formatTime

class ArticleItemAdapter(private val context: Context) :
    RecyclerView.Adapter<ArticleItemAdapter.ViewHolder>() {

    private val articleList =
        SortedList(ArticleItem::class.java, object : SortedListAdapterCallback<ArticleItem>(this) {
            override fun compare(o1: ArticleItem, o2: ArticleItem): Int {
                return o2.id.compareTo(o1.id) // 倒序
            }

            override fun areContentsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(item1: ArticleItem, item2: ArticleItem): Boolean {
                return item1.id == item2.id
            }
        })

    fun addAll(articleItem: ArticleItem) {
        articleList.add(articleItem)
    }

    fun addAll(list: List<ArticleItem>) {
        articleList.addAll(list)
    }

    fun reAddAll(list: List<ArticleItem>) {
        articleList.clear()
        articleList.addAll(list)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val container: ConstraintLayout = view.findViewById(R.id.container)
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val containerTextView: TextView = view.findViewById(R.id.containerTextView)
        val idTextView: TextView = view.findViewById(R.id.idTextView)
        val timeTextView: TextView = view.findViewById(R.id.timeTextView)
        val favoriteNumberTextView: TextView = view.findViewById(R.id.favoriteNumberTextView)
        val favoriteImageView: ImageView = view.findViewById(R.id.favoriteImageView)
        val disFavoriteImageView: ImageView = view.findViewById(R.id.disFavoriteImageView)
        val readNumberTextView: TextView = view.findViewById(R.id.readNumberTextView)
        val backgroundImageView: ImageView = view.findViewById(R.id.backgroundImageView)
        val authorTextView: TextView = view.findViewById(R.id.authorTextView)
        val authorImageView: ImageView = view.findViewById(R.id.authorImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articleList[position]
        holder.apply {
            container.setOnClickListener {
                val intent = Intent(context, ContainerActivity::class.java)
                intent.putExtra("article_id", article.id)
                context.startActivity(intent)
            }
            titleTextView.text = article.title
            containerTextView.text = article.container
            idTextView.text = "id:${article.id}"
            timeTextView.text = article.time.formatTime()
            favoriteNumberTextView.text = article.favoriteNumber.format()
            if (article.favorite) {
                favoriteImageView.visibility = View.VISIBLE
                disFavoriteImageView.visibility = View.GONE
            } else {
                favoriteImageView.visibility = View.GONE
                disFavoriteImageView.visibility = View.VISIBLE
            }
            readNumberTextView.text = article.readNumber.format()
            authorTextView.text = article.authorName
            if (article.image.isNullOrEmpty()) {
                backgroundImageView.visibility = View.GONE
            } else {
                backgroundImageView.visibility = View.VISIBLE
                Glide.with(backgroundImageView.context)
                    .load(article.image)
                    .placeholder(R.drawable.ic_image_loading)
                    .error(R.drawable.ic_image_error)
                    .thumbnail(0.8f)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .transform(CenterInside())
                    .into(backgroundImageView)
            }
            if (article.authorPicture.isNullOrEmpty()) {
                authorImageView.visibility = View.GONE
            } else {
                authorImageView.visibility = View.VISIBLE
                Glide.with(backgroundImageView.context)
                    .load(article.authorPicture)
                    .placeholder(R.drawable.ic_drawer_user)
                    .error(R.drawable.ic_image_error)
                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .transform(CenterInside(), CircleCrop())
                    .into(authorImageView)
            }
        }
    }

    override fun getItemCount(): Int = articleList.size()
}