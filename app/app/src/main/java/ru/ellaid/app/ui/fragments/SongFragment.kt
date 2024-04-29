package ru.ellaid.app.ui.fragments

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import ru.ellaid.app.R
import ru.ellaid.app.data.entity.Track
import ru.ellaid.app.exoplayer.isPlaying
import ru.ellaid.app.exoplayer.toSong
import ru.ellaid.app.other.Status
import ru.ellaid.app.ui.viewmodels.MainViewModel
import ru.ellaid.app.ui.viewmodels.SongViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.ellaid.app.databinding.FragmentSongBinding
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SongFragment : Fragment(R.layout.fragment_song) {
    private lateinit var binding: FragmentSongBinding

    @Inject
    lateinit var glide: RequestManager

    private lateinit var mainViewModel: MainViewModel
    private val songViewModel: SongViewModel by viewModels()

    private var curPlayingTrack: Track? = null

    private var playbackState: PlaybackStateCompat? = null

    private var shouldUpdateSeekBar = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSongBinding.bind(view)
        super.onViewCreated(binding.root, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        subscribeToObservers()

        binding.ivPlayPauseDetail.setOnClickListener {
            curPlayingTrack?.let {
                mainViewModel.playOrToggleSong(it, true)
            }
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    setCurPlayerTimeToTextView(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                shouldUpdateSeekBar = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    mainViewModel.seekTo(it.progress.toLong())
                    shouldUpdateSeekBar = true
                }
            }
        })

        binding.ivSkipPrevious.setOnClickListener {
            mainViewModel.skipToPreviousSong()
        }

        binding.ivSkip.setOnClickListener {
            mainViewModel.skipToNextSong()
        }

        binding.ivComment.isEnabled = false
    }

    private fun updateSongData(track: Track) {
        binding.tvSongName.text = track.name
        binding.tvSongAuthor.text = track.author
        glide.load(track.coverUrl).into(binding.ivSongImage)
        binding.ivComment.apply {
            isEnabled = true
            setOnClickListener {
                val sheet = CommentsFragment(track.id)
                fragmentManager?.let { sheet.show(it, sheet.tag) }
            }
        }
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) {
            it?.let {result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        result.data?.let {songs ->
                            if (curPlayingTrack == null && songs.isNotEmpty()) {
                                curPlayingTrack = songs[0]
                                updateSongData(songs[0])
                            }
                        }
                    }
                    else -> Unit
                }
            }
        }
        mainViewModel.curPlayingSong.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            curPlayingTrack = it.toSong()
            updateSongData(curPlayingTrack!!)
        }
        mainViewModel.playbackState.observe(viewLifecycleOwner) {
            playbackState = it
            binding.ivPlayPauseDetail.setImageResource(
                if (playbackState?.isPlaying == true) R.drawable.ic_pause else R.drawable.ic_play
            )
            binding.seekBar.progress = it?.position?.toInt() ?: 0
        }
        songViewModel.curPlayerPosition.observe(viewLifecycleOwner) {
            if (shouldUpdateSeekBar) {
                binding.seekBar.progress = it.toInt()
                setCurPlayerTimeToTextView(it)
            }
        }
        songViewModel.curSongDuration.observe(viewLifecycleOwner) {
            binding.seekBar.max = it.toInt()
            val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
            binding.tvSongDuration.text = dateFormat.format(it)
        }
    }

    private fun setCurPlayerTimeToTextView(ms: Long) {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        binding.tvCurTime.text = dateFormat.format(ms)
    }
}