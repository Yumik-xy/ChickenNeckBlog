package com.yumik.chickenneckblog.ui.main.fragment.follow

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
import com.yumik.chickenneckblog.utils.formatTime

class FollowAdapter(private val articleList: List<Article>) :
    RecyclerView.Adapter<FollowAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val containerTextView: TextView = view.findViewById(R.id.containerTextView)
        val countTextView: TextView = view.findViewById(R.id.idTextView)
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
        holder.titleTextView.text = article.title
        holder.containerTextView.text = article.container
        holder.countTextView.text = (position + 1).toString().padStart(2, '0')
        holder.timeTextView.text = article.time.formatTime()
        holder.loveNumberTextView.text = "${article.loveNumber}"
        holder.readNumberTextView.text = "${article.readNumber}"
        holder.authorTextView.text = article.authorName
        Glide.with(holder.backgroundImageView.context)
            .load(article.image)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .transform(CenterInside())
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.backgroundImageView.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.backgroundImageView.visibility = View.VISIBLE
                    return false
                }
            })
            .into(holder.backgroundImageView)
        Glide.with(holder.authorImageView.context)
            .load(article.image)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .transform(CenterInside(), CircleCrop())
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.authorImageView.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.authorImageView.visibility = View.VISIBLE
                    return false
                }
            })
            .into(holder.authorImageView)
    }

    override fun getItemCount(): Int = articleList.size
}