package ru.ellaid.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import ru.ellaid.app.R
import ru.ellaid.app.data.entity.Track
import ru.ellaid.app.databinding.ListItemBinding
import javax.inject.Inject

class SearchAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var tracks: List<Track> = emptyList()

    private var onItemClickListener: ((Track) -> Unit)? = null

    fun setItemClickListener(listener: (Track) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val binding = ListItemBinding.bind(holder.itemView)
        val song = tracks[position]

        binding.apply {
            tvPrimary.text = song.name
            tvSecondary.text = song.author
            glide.load(song.coverUrl).into(ivItemImage)
            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}