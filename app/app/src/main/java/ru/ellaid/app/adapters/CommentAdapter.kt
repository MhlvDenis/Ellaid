package ru.ellaid.app.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ru.ellaid.app.R
import ru.ellaid.app.data.entity.Comment
import ru.ellaid.app.databinding.CommentItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var comments: List<Comment> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.comment_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val binding = CommentItemBinding.bind(holder.itemView)
        val comment = comments[position]
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        binding.apply {
            tvUsername.text = comment.username
            tvDatetime.text = dateFormat.format(
                Date(comment.publicationDatetime.epochSecond)
            )
            tvContent.text = comment.content
        }
    }
}