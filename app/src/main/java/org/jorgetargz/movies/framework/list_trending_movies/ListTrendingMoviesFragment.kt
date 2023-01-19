package org.jorgetargz.movies.framework.list_trending_movies

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.jorgetargz.movies.R
import org.jorgetargz.movies.databinding.FragmentTrendingMoviesBinding
import org.jorgetargz.movies.framework.main.MainActivity


@AndroidEntryPoint
class ListTrendingMoviesFragment : Fragment(), MenuProvider {

    private lateinit var binding: FragmentTrendingMoviesBinding
    private val viewModel: ListTrendingMoviesViewModel by viewModels()
    private lateinit var adapter: MoviesAdapter

    inner class ListTrendingMoviesActionsImpl : ListTrendingMoviesActions {
        override fun onMovieClicked(id: Int) {
            val action = ListTrendingMoviesFragmentDirections.actionTrendingMoviesFragmentToDetailsMovieFragment(id)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setupBinding()
        setupAdapter()
        addMenuProvider()

        viewModel.handleEvent(ListTrendingMoviesContract.ListTrendingMoviesEvent.LoadTrendingMovies)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { value ->
                    value.moviesFiltered.let { movieList ->
                        adapter.submitList(movieList)
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
        binding = FragmentTrendingMoviesBinding.inflate(layoutInflater)
    }

    private fun setupAdapter() {
        val rvMovies = binding.rvMovies
        adapter = MoviesAdapter(ListTrendingMoviesActionsImpl())
        rvMovies.adapter = adapter
    }

    private fun addMenuProvider() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_buscador, menu)
        val actionSearch = menu.findItem(R.id.search).actionView as SearchView
        actionSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.handleEvent(
                        ListTrendingMoviesContract.ListTrendingMoviesEvent.FilterTrendingMovies(
                            newText
                        )
                    )
                }
                return false
            }
        })
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }


}