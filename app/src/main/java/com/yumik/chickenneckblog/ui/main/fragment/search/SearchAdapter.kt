package com.yumik.chickenneckblog.ui.main.fragment.search

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
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
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.yumik.chickenneckblog.R
import com.yumik.chickenneckblog.logic.model.Article
import com.yumik.chickenneckblog.ui.container.ContainerActivity
import com.yumik.chickenneckblog.utils.formatTime

class SearchAdapter(private val context: Context) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val articleList =
        SortedList(Article::class.java, object : SortedListAdapterCallback<Article>(this) {
            override fun compare(o1: Article, o2: Article): Int {
                return o2.id.compareTo(o1.id) // 倒序
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(item1: Article, item2: Article): Boolean {
                return item1.id == item2.id
            }
        })

    fun add(article: Article) {
        articleList.add(article)
    }

    fun addAll(list: List<Article>) {
        articleList.addAll(list)
    }

    fun reAddAll(list: List<Article>) {
        articleList.clear()
        articleList.addAll(list)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val container: ConstraintLayout = view.findViewById(R.id.container)
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val containerTextView: TextView = view.findViewById(R.id.containerTextView)
        val idTextView: TextView = view.findViewById(R.id.idTextView)
        val timeTextView: TextView = view.findViewById(R.id.timeTextView)
        val loveNumberTextView: TextView = view.findViewById(R.id.loveNumberTextView)
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articleList[position]
        holder.container.setOnClickListener {
            val intent = Intent(context, ContainerActivity::class.java)
            intent.putExtra("article_id", article.id)
            context.startActivity(intent)
        }
        holder.titleTextView.text = article.title
        holder.containerTextView.text = article.container
        holder.idTextView.text = "id:${article.id}"
        holder.timeTextView.text = article.time.formatTime()
        holder.loveNumberTextView.text = "${article.loveNumber}"
        holder.readNumberTextView.text = "${article.readNumber}"
        holder.authorTextView.text = article.authorName
        if (article.image.isNullOrEmpty()) {
            holder.backgroundImageView.visibility = View.GONE
        } else {
            holder.backgroundImageView.visibility = View.VISIBLE
            Glide.with(holder.backgroundImageView.context)
                .load(article.image)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(CenterInside())
                .into(holder.backgroundImageView)
        }
        if (article.authorPicture.isNullOrEmpty()) {
            holder.authorImageView.visibility = View.GONE
        } else {
            holder.authorImageView.visibility = View.VISIBLE
            Glide.with(holder.backgroundImageView.context)
                .load(article.authorPicture)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(CenterInside(), CircleCrop())
                .into(holder.authorImageView)
        }
    }

    override fun getItemCount(): Int = articleList.size()
}