package ru.ellaid.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.ellaid.app.data.entity.Track

abstract class BaseSongAdapter(
    private val layoutId: Int,
) : RecyclerView.Adapter<BaseSongAdapter.SongViewHolder>() {
    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    protected val diffCallback = object : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    protected abstract val differ: AsyncListDiffer<Track>

    var tracks: List<Track>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layoutId,
                parent,
                false
            )
        )
    }

    protected var onItemClickListener: ((Track) -> Unit)? = null

    fun setItemClickListener(listener: (Track) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}
