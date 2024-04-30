package ru.ellaid.app.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import com.bumptech.glide.RequestManager
import ru.ellaid.app.R
import ru.ellaid.app.databinding.ListItemBinding
import javax.inject.Inject

class TrackAdapter @Inject constructor(
    private val glide: RequestManager
) : BaseSongAdapter(R.layout.list_item) {
    override val differ = AsyncListDiffer(this, diffCallback)

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
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
}
