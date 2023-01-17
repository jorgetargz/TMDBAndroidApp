package org.jorgetargz.movies.framework.list_trending_tv_shows

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jorgetargz.movies.domain.use_cases.tv_shows.LoadCachedTrendingTVShowsUseCase
import org.jorgetargz.movies.domain.use_cases.tv_shows.LoadTrendingTVShowsUseCase
import org.jorgetargz.movies.framework.utils.Utils
import org.jorgetargz.movies.utils.NetworkResult
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ListTrendingTVShowsViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val loadTrendingTVShowsUseCase: LoadTrendingTVShowsUseCase,
    private val loadCachedTrendingTVShowsUseCase: LoadCachedTrendingTVShowsUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<ListTrendingTVShowsContract.ListTrendingTVShowsState> by lazy {
        MutableStateFlow(ListTrendingTVShowsContract.ListTrendingTVShowsState())
    }
    val uiState: StateFlow<ListTrendingTVShowsContract.ListTrendingTVShowsState> = _uiState

    private fun loadTrendingTVShows() {
        viewModelScope.launch {
            if (Utils.hasInternetConnection(context = appContext)) {
                loadTrendingTVShowsUseCase.invoke()
                    .catch(action = { cause ->
                        _uiState.update {
                            it.copy(
                                error = cause.message,
                                isLoading = false
                            )
                        }
                    })
                    .collect { result ->
                        when (result) {
                            is NetworkResult.Error -> {
                                _uiState.update {
                                    it.copy(
                                        error = result.message,
                                        isLoading = false
                                    )
                                }
                            }
                            is NetworkResult.Loading -> _uiState.update {
                                it.copy(isLoading = true)
                            }
                            is NetworkResult.Success -> _uiState.update {
                                it.copy(
                                    tvShows = result.data ?: emptyList(),
                                    tvShowsFiltered = result.data ?: emptyList(),
                                    isLoading = false
                                )
                            }
                        }
                    }
            } else {
                loadCachedTrendingTVShowsUseCase.invoke()
                    .catch(action = { cause ->
                        _uiState.update {
                            it.copy(
                                error = cause.message,
                                isLoading = false
                            )
                        }
                    })
                    .collect { result ->
                        when (result) {
                            is NetworkResult.Error -> {
                                _uiState.update {
                                    it.copy(
                                        error = result.message,
                                        isLoading = false
                                    )
                                }
                            }
                            is NetworkResult.Loading -> _uiState.update {
                                it.copy(isLoading = true)
                            }
                            is NetworkResult.Success -> _uiState.update {
                                it.copy(
                                    error = "Loaded from cache",
                                    tvShows = result.data ?: emptyList(),
                                    tvShowsFiltered = result.data ?: emptyList(),
                                    isLoading = false
                                )
                            }
                        }
                    }
            }
        }
    }

    private fun filterTVShows(nombre: String) {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        tvShowsFiltered = _uiState.value.tvShows.filter { movie ->
                            movie.name.contains(nombre, true)
                        }
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun handleEvent(event: ListTrendingTVShowsContract.ListTrendingTVShowsEvent) {
        when (event) {
            is ListTrendingTVShowsContract.ListTrendingTVShowsEvent.LoadTrendingTVShows -> loadTrendingTVShows()
            is ListTrendingTVShowsContract.ListTrendingTVShowsEvent.FilterTrendingTVShows -> filterTVShows(
                event.nombre
            )
        }
    }
}