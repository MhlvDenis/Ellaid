package ru.ellaid.app.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import ru.ellaid.app.R
import ru.ellaid.app.databinding.SwipeItemBinding

class SwipeSongAdapter : BaseSongAdapter(R.layout.swipe_item) {
    override val differ = AsyncListDiffer(this, diffCallback)

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val binding = SwipeItemBinding.bind(holder.itemView)
        val song = tracks[position]
        binding.apply {
            val text = "${song.name} - ${song.author}"
            tvPrimary.text = text

            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                }
            }
        }
    }
}
