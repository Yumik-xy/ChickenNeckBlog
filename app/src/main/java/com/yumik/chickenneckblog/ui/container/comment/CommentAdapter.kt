package com.yumik.chickenneckblog.ui.container.comment

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.yumik.chickenneckblog.R
import com.yumik.chickenneckblog.logic.model.SelectedComment
import com.yumik.chickenneckblog.utils.formatTime

class CommentAdapter(private val context: Context, private val articleId: Int) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    companion object {
        private const val NORMAL_VIEW = 10000
        private const val FOOT_VIEW = 10001
        private const val FOOT_TEXT_NORMAL = "查看所有评论　>"
        private const val FOOT_TEXT_LOADING = "正在加载评论"
        private const val FOOT_TEXT_NO_MORE = "没有更多评论了"
        private const val TAG = "CommentAdapter"
    }

    private var footHolder: CommentAdapter.CommentViewHolder? = null

    inner class CommentViewHolder(view: View, viewType: Int) : RecyclerView.ViewHolder(view) {

        val container: LinearLayout = view.findViewById(R.id.container)
        val userPictureImageView: ImageView = view.findViewById(R.id.userPictureImageView)
        val userNameTextView: TextView = view.findViewById(R.id.userNameTextView)
        val createTimeTextView: TextView = view.findViewById(R.id.createTimeTextView)
        val commentTextView: TextView = view.findViewById(R.id.commentTextView)
        val footTextView: TextView = view.findViewById(R.id.footTextView)
        val footLayout: LinearLayout = view.findViewById(R.id.footLayout)
        val footProgressBar: androidx.core.widget.ContentLoadingProgressBar =
            view.findViewById(R.id.footProgressBar)

        init {
            if (viewType != NORMAL_VIEW) {
                footProgressBar.hide()
            }
        }
    }

//    inner class ReplyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val userPictureImageView: ImageView = view.findViewById(R.id.userPictureImageView)
//        val userNameTextView: TextView = view.findViewById(R.id.userNameTextView)
//        val createTimeTextView: TextView = view.findViewById(R.id.createTimeTextView)
//        val commentTextView: TextView = view.findViewById(R.id.commentTextView)
//        val loadMoreTextView: TextView = view.findViewById(R.id.loadMoreTextView)
//    }

    private val list =
        SortedList(
            SelectedComment::class.java,
            object : SortedListAdapterCallback<SelectedComment>(this) {
                override fun compare(o1: SelectedComment, o2: SelectedComment): Int {
                    return o1.id.compareTo(o2.id)
                }

                override fun areContentsTheSame(
                    oldItem: SelectedComment,
                    newItem: SelectedComment
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areItemsTheSame(
                    item1: SelectedComment,
                    item2: SelectedComment
                ): Boolean {
                    return item1.id == item2.id
                }
            })

    fun addAll(selectedComment: SelectedComment) {
        list.add(selectedComment)
    }

    fun addAll(list: List<SelectedComment>) {
        this.list.addAll(list)
    }

    fun reAddAll(list: List<SelectedComment>) {
        this.list.clear()
        this.list.addAll(list)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) FOOT_VIEW else NORMAL_VIEW
    }

    override fun getItemCount(): Int {
        return list.size() + 1
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        if (getItemViewType(position) == NORMAL_VIEW) {
            val item = list[position]
            holder.apply {
                userNameTextView.text = item.userName
                commentTextView.text = item.content
                createTimeTextView.text = item.createTime.formatTime()
                if (item.userPicture.isNullOrEmpty()) {
                    userPictureImageView.visibility = View.GONE
                } else {
                    userPictureImageView.visibility = View.VISIBLE
                    Glide.with(userPictureImageView.context)
                        .load(item.userPicture)
                        .placeholder(R.drawable.ic_drawer_user)
                        .error(R.drawable.ic_image_error)
                        .thumbnail(0.1f)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .transform(CenterInside(), CircleCrop())
                        .into(userPictureImageView)
                }
                container.setOnClickListener {
                    val intent = Intent(context, CommentActivity::class.java)
                    intent.putExtra("comment_id", item.id)
                    intent.putExtra("article_id", articleId)
                }
            }
        } else {
            holder.apply {
                footTextView.text = "查看所有评论　>"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        if (viewType == NORMAL_VIEW) {
            val root = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)
            val holder = CommentViewHolder(root, viewType)
            return holder
        } else {
            val root = LayoutInflater.from(context).inflate(R.layout.item_foot_more, parent, false)
            val holder = CommentViewHolder(root, viewType)
            footHolder = holder
            return holder
        }
    }

    fun startLoading() {
        footHolder?.let {
            it.footTextView.text
            it.footLayout.visibility = View.VISIBLE
            it.footProgressBar.show()
        }
    }

    fun stopLoading() {
        footHolder?.let {
            it.footTextView.text
            it.footLayout.visibility = View.VISIBLE
            it.footProgressBar.show()
        }
    }
}
