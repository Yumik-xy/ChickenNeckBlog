package com.yumik.chickenneckblog.ui.container

import android.content.Context
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
import com.yumik.chickenneckblog.logic.model.Comment
import com.yumik.chickenneckblog.utils.formatTime
import com.yumik.chickenneckblog.utils.setOnUnShakeClickListener

class ContainerAdapter(private val context: Context, private val block: (() -> Unit)?) :
    RecyclerView.Adapter<ContainerAdapter.CommentViewHolder>() {

    companion object {
        private const val NORMAL_VIEW = 10000
        private const val FOOT_VIEW = 10001

        private const val TAG = "ContainerAdapter"
    }

    private var footHolder: ContainerAdapter.CommentViewHolder? = null

    inner class CommentViewHolder(view: View, viewType: Int) : RecyclerView.ViewHolder(view) {

        lateinit var footTextView: TextView
        lateinit var footLayout: LinearLayout

        //        lateinit var footProgressBar: ContentLoadingProgressBar
        lateinit var userPictureImageView: ImageView
        lateinit var userNameTextView: TextView
        lateinit var createTimeTextView: TextView
        lateinit var commentTextView: TextView

        init {
            if (viewType == FOOT_VIEW) {
                footTextView = view.findViewById(R.id.footTextView)
                footLayout = view.findViewById(R.id.footLayout)
//                footProgressBar =
//                    view.findViewById(R.id.footProgressBar)
//                footProgressBar.hide()
            } else {
                userPictureImageView = view.findViewById(R.id.userPictureImageView)
                userNameTextView = view.findViewById(R.id.userNameTextView)
                createTimeTextView = view.findViewById(R.id.createTimeTextView)
                commentTextView = view.findViewById(R.id.commentTextView)
            }
        }
    }

    private val list =
        SortedList(
            Comment::class.java,
            object : SortedListAdapterCallback<Comment>(this) {
                override fun compare(o1: Comment, o2: Comment): Int {
                    return o1.id.compareTo(o2.id)
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
            }
        } else {
            holder.apply {
                footTextView.text = if (list.size() == 0) "马上去抢沙发　>" else "查看所有评论　>"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        if (viewType == NORMAL_VIEW) {
            val root = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)
            val holder = CommentViewHolder(root, viewType)
            root.setOnUnShakeClickListener {
                block?.invoke()
            }
            return holder
        } else {
            val root = LayoutInflater.from(context).inflate(R.layout.item_foot_more, parent, false)
            val holder = CommentViewHolder(root, viewType)
            root.setOnClickListener {
                block?.invoke()
            }
            footHolder = holder
            return holder
        }
    }
}
