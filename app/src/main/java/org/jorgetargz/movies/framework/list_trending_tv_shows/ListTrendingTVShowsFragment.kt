package org.jorgetargz.movies.framework.list_trending_tv_shows

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.jorgetargz.movies.R
import org.jorgetargz.movies.databinding.FragmentTrendingTvShowsBinding
import org.jorgetargz.movies.framework.main.MainActivity


@AndroidEntryPoint
class ListTrendingTVShowsFragment : Fragment(), MenuProvider {

    private lateinit var binding: FragmentTrendingTvShowsBinding
    private val viewModel: ListTrendingTVShowsViewModel by viewModels()
    private lateinit var adapter: TVShowsAdapter

    inner class ListTrendingTVShowsActionsImpl : ListTrendingTVShowsActions {
        override fun onTVShowClicked(id: Int) {
            //TODO: Implement this
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setupBinding()
        setupAdapter()
        addMenuProvider()

        viewModel.handleEvent(ListTrendingTVShowsContract.ListTrendingTVShowsEvent.LoadTrendingTVShows)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { value ->
                    value.tvShowsFiltered.let { movieList ->
                        adapter.submitList(movieList)
                    }
                    value.error?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
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
        binding = FragmentTrendingTvShowsBinding.inflate(layoutInflater)
    }

    private fun setupAdapter() {
        val rvTVShows = binding.rvTVShows
        adapter = TVShowsAdapter(ListTrendingTVShowsActionsImpl())
        rvTVShows.adapter = adapter
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
                        ListTrendingTVShowsContract.ListTrendingTVShowsEvent.FilterTrendingTVShows(
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