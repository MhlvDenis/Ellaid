package ru.ellaid.app.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.ellaid.app.R
import ru.ellaid.app.adapters.TrackAdapter
import ru.ellaid.app.other.Status
import ru.ellaid.app.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.ellaid.app.databinding.FragmentHomeBinding
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var trackAdapter: TrackAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentHomeBinding.bind(view)
        super.onViewCreated(binding.root, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        setupRecyclerView()
        subscribeToObservers()

        trackAdapter.setItemClickListener {
            mainViewModel.playOrToggleSong(it)
        }
    }

    private fun setupRecyclerView() = binding.rvAllSongs.apply {
        adapter = trackAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    binding.allSongsProgressBar.isVisible = false
                    result.data?.let { songs ->
                        trackAdapter.tracks = songs
                    }
                }
                Status.ERROR -> Unit
                Status.LOADING -> binding.allSongsProgressBar.isVisible = true
            }
        }
    }
}