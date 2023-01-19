package org.jorgetargz.movies.framework.details_movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.jorgetargz.movies.R
import org.jorgetargz.movies.databinding.FragmentDetailsMovieBinding
import org.jorgetargz.movies.framework.common.Constantes
import org.jorgetargz.movies.framework.main.MainActivity
import org.jorgetargz.movies.framework.utils.loadUrlFromTMDBW342Size


@AndroidEntryPoint
class DetailsMovieFragment : Fragment() {

    private lateinit var binding: FragmentDetailsMovieBinding
    private val viewModel: DetailsMovieViewModel by viewModels()

    private val argIdMovie = Constantes.ID_MOVIE
    private var idMovie: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idMovie = it.getInt(argIdMovie)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setupBinding()

        viewModel.handleEvent(DetailsMovieContract.DetailsMovieEvent.LoadMovie(idMovie))

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { value ->
                    value.movie.let { movie ->
                        with(binding) {
                            movie.posterPath?.let { ivPoster.loadUrlFromTMDBW342Size(it) }
                            tvValueTitle.text = movie.title
                            tvValueOverview.text = movie.overview
                            tvValueReleaseDate.text = movie.releaseDate.toString()
                            tvValueVoteAverage.text = movie.voteAverage.toString()
                            tvValueVoteCount.text = movie.voteCount.toString()
                        }
                    }
                    value.error?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                    }
                    value.isLoading.let {
                        ((activity as MainActivity).findViewById(R.id.loadingAnimation) as ProgressBar)
                            .visibility = if (it) View.VISIBLE else View.GONE
                    }
                }
            }
        }

        return binding.root
    }

    private fun setupBinding() {
        binding = FragmentDetailsMovieBinding.inflate(layoutInflater)
    }
}