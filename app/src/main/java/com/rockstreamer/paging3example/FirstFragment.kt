package com.rockstreamer.paging3example

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.rockstreamer.adsmodule.FacebookNativeAdAdapter
import com.rockstreamer.paging3example.databinding.FragmentFirstBinding
import com.rockstreamer.paging3example.ui.adapters.MovieAdapter
import com.training.pagingsample.ui.adapters.MovieLoadStateAdapter
import com.rockstreamer.paging3example.ui.screen.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val mainBinding get() = _binding!!

    private val viewModel: MainViewModel by viewModel()
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        movieAdapter = MovieAdapter()

        mainBinding.movieRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = movieAdapter.withLoadStateFooter(
                footer = MovieLoadStateAdapter { movieAdapter.retry() }
            )
        }

        lifecycleScope.launch {
            viewModel.movies.collectLatest {
                movieAdapter.submitData(it)
            }
        }

        mainBinding.btnRetry.setOnClickListener{
            movieAdapter.retry()
        }

        // show the loading state for te first load
        movieAdapter.addLoadStateListener { loadState ->

            if (loadState.refresh is LoadState.Loading) {

                mainBinding.btnRetry.visibility = View.GONE

                // Show ProgressBar
                mainBinding.progressBar.visibility = View.VISIBLE
            }
            else {
                // Hide ProgressBar
                mainBinding.progressBar.visibility = View.GONE

                // If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> {
                        mainBinding.btnRetry.visibility = View.VISIBLE
                        loadState.refresh as LoadState.Error
                    }
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(requireActivity(), it.error.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        return mainBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}