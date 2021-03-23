package com.yumik.chickenneckblog.ui.container.comment

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.yumik.chickenneckblog.R
import com.yumik.chickenneckblog.logic.model.Comment
import com.yumik.chickenneckblog.utils.formatTime
import java.io.Serializable

class CommentAdapter(
    private val context: Context,
    private val articleId: Int
) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    companion object {
        private const val TAG = "ContainerAdapter"
    }

    inner class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val userPictureImageView: ImageView = view.findViewById(R.id.userPictureImageView)
        val userNameTextView: TextView = view.findViewById(R.id.userNameTextView)
        val createTimeTextView: TextView = view.findViewById(R.id.createTimeTextView)
        val commentTextView: TextView = view.findViewById(R.id.commentTextView)
        val secondaryCommentLayout: ViewGroup = view.findViewById(R.id.secondaryCommentLayout)
    }

    private val list =
        SortedList(
            Comment::class.java,
            object : SortedListAdapterCallback<Comment>(this) {
                override fun compare(o1: Comment, o2: Comment): Int {
                    return o2.id.compareTo(o1.id)
                }

                override fun areContentsTheSame(
                    oldItem: Comment,
                    newItem: Comment
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areItemsTheSame(
                    item1: Comment,
                    item2: Comment
                ): Boolean {
                    return item1.id == item2.id
                }
            })

    fun add(Comment: Comment) {
        list.add(Comment)
    }

    fun addAll(list: List<Comment>) {
        this.list.addAll(list)
    }

    fun reAddAll(list: List<Comment>) {
        this.list.clear()
        this.list.addAll(list)
    }

    override fun getItemCount(): Int {
        return list.size()
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
            val item = list[position]
            holder.apply {
                userNameTextView.text = item.user.name
                commentTextView.text = item.content
                createTimeTextView.text = item.time.formatTime()
                if (item.user.picture.isNullOrEmpty()) {
                    userPictureImageView.visibility = View.GONE
                } else {
                    userPictureImageView.visibility = View.VISIBLE
                    Glide.with(userPictureImageView.context)
                        .load(item.user.picture)
                        .placeholder(R.drawable.ic_drawer_user)
                        .error(R.drawable.ic_image_error)
                        .thumbnail(0.1f)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .transform(CenterInside(), CircleCrop())
                        .into(userPictureImageView)
                }
                if (item.commentList != null) {
                    Log.d(TAG, item.commentNumber.toString())
                    Log.d(TAG, item.commentList.toString())
                    val recyclerView = RecyclerView(context)
                    recyclerView.layoutManager = object : LinearLayoutManager(context) {
                        override fun canScrollVertically(): Boolean {
                            return false
                        }
                    }
                    val adapter = SecondaryCommentAdapter(context) {
                        val intent = Intent(context, CommentActivity::class.java)
                        intent.putExtra("article_id", articleId)
//                        intent.putExtra("comment_id", item.id)
                        intent.putExtra("data",item as Serializable)
                        context.startActivity(intent)
                    }
                    recyclerView.adapter = adapter
                    adapter.addAll(item.commentList, item.commentNumber)
                    secondaryCommentLayout.removeAllViews()
                    secondaryCommentLayout.addView(recyclerView)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
            val root = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)
            return CommentViewHolder(root)
    }
}
