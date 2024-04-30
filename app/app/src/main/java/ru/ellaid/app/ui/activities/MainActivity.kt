package ru.ellaid.app.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import ru.ellaid.app.R
import ru.ellaid.app.adapters.SwipeSongAdapter
import ru.ellaid.app.data.entity.Track
import ru.ellaid.app.exoplayer.isPlaying
import ru.ellaid.app.exoplayer.toSong
import ru.ellaid.app.common.Status
import ru.ellaid.app.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.ellaid.app.databinding.ActivityMainBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var swipeSongAdapter: SwipeSongAdapter

    @Inject
    lateinit var glide: RequestManager

    private var curPlayingTrack: Track? = null

    private var playbackState: PlaybackStateCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribeToObservers()

        binding.vpSong.adapter = swipeSongAdapter

        binding.vpSong.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (playbackState?.isPlaying == true) {
                    mainViewModel.playOrToggleSong(swipeSongAdapter.tracks[position])
                } else {
                    curPlayingTrack = swipeSongAdapter.tracks[position]
                }
            }
        })

        binding.ivPlayPause.setOnClickListener {
            curPlayingTrack?.let {
                mainViewModel.playOrToggleSong(it, true)
            }
        }

        swipeSongAdapter.setItemClickListener {
            binding.navHostFragment.getFragment<Fragment>().findNavController().navigate(
                R.id.globalActionToSongFragment
            )
        }

        binding.ivSearch.setOnClickListener {
            binding.navHostFragment.getFragment<Fragment>().findNavController().navigate(
                R.id.globalActionToSearchFragment
            )
        }

        binding.navHostFragment.getFragment<Fragment>().findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.songFragment -> {
                        hideTopBar()
                        hideBottomBar()
                    }
                    R.id.homeFragment -> {
                        showTopBar()
                        showBottomBar()
                    }
                    R.id.searchFragment -> {
                        hideTopBar()
                        showBottomBar()
                    }
                    else -> {
                        showTopBar()
                        showBottomBar()
                    }
                }
            }
    }

    private fun hideTopBar() {
        binding.ivUser.isVisible = false
        binding.tvTitle.isVisible = false
        binding.ivSearch.isVisible = false
    }

    private fun showTopBar() {
        binding.ivUser.isVisible = true
        binding.tvTitle.isVisible = true
        binding.ivSearch.isVisible = true
    }

    private fun hideBottomBar() {
        binding.ivCurSongImage.isVisible = false
        binding.vpSong.isVisible = false
        binding.ivPlayPause.isVisible = false
    }

    private fun showBottomBar() {
        binding.ivCurSongImage.isVisible = true
        binding.vpSong.isVisible = true
        binding.ivPlayPause.isVisible = true
    }

    private fun switchViewPagerToCurrentSong(track: Track) {
        val newItemIndex = swipeSongAdapter.tracks.indexOf(track)
        if (newItemIndex != -1) {
            binding.vpSong.currentItem = newItemIndex
            curPlayingTrack = track
        }
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        result.data?.let { songs ->
                            swipeSongAdapter.tracks = songs
                            if (songs.isNotEmpty()) {
                                glide.load((curPlayingTrack ?: songs[0]).coverUrl)
                                    .into(binding.ivCurSongImage)
                            }
                            switchViewPagerToCurrentSong(curPlayingTrack ?: return@observe)
                        }
                    }
                    Status.ERROR -> Unit
                    Status.LOADING -> Unit
                }
            }
        }
        mainViewModel.curPlayingSong.observe(this) {
            if (it == null) return@observe

            curPlayingTrack = it.toSong()
            glide.load(curPlayingTrack?.coverUrl).into(binding.ivCurSongImage)
            switchViewPagerToCurrentSong(curPlayingTrack ?: return@observe)
        }
        mainViewModel.playbackState.observe(this) {
            playbackState = it
            binding.ivPlayPause.setImageResource(
                if (playbackState?.isPlaying == true) R.drawable.ic_pause else R.drawable.ic_play
            )
        }
        mainViewModel.isConnected.observe(this) {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.ERROR -> Snackbar.make(
                        binding.rootLayout,
                        result.message ?: "An unknown error occurred",
                        Snackbar.LENGTH_LONG
                    ).show()
                    else -> Unit
                }
            }
        }
        mainViewModel.networkError.observe(this) {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.ERROR -> Snackbar.make(
                        binding.rootLayout,
                        result.message ?: "An unknown error occured",
                        Snackbar.LENGTH_LONG
                    ).show()
                    else -> Unit
                }
            }
        }
    }
}