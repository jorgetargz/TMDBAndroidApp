package org.jorgetargz.movies.framework.details_tv_show

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
import org.jorgetargz.movies.databinding.FragmentDetailsTvShowBinding
import org.jorgetargz.movies.framework.common.Constantes
import org.jorgetargz.movies.framework.main.MainActivity
import org.jorgetargz.movies.framework.utils.loadUrlFromTMDBW342Size


@AndroidEntryPoint
class DetailsTVShowFragment : Fragment() {

    private lateinit var binding: FragmentDetailsTvShowBinding
    private val viewModel: DetailsTVShowViewModel by viewModels()

    private val argIdTVShow = Constantes.ID_TV_SHOW
    private var idTVShow: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idTVShow = it.getInt(argIdTVShow)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setupBinding()

        viewModel.handleEvent(DetailsTVShowContract.DetailsTVShowEvent.LoadTVShow(idTVShow))

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { value ->
                    value.tvShow.let { tvShow ->
                        with(binding) {
                            tvShow.posterPath?.let { ivPoster.loadUrlFromTMDBW342Size(it) }
                            tvValueName.text = tvShow.name
                            tvValueOverview.text = tvShow.overview
                            tvValueFirstAir.text = tvShow.firstAirDate.toString()
                            tvValueVoteAverage.text = tvShow.voteAverage.toString()
                            tvValueVoteCount.text = tvShow.voteCount.toString()
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
        binding = FragmentDetailsTvShowBinding.inflate(layoutInflater)
    }
}