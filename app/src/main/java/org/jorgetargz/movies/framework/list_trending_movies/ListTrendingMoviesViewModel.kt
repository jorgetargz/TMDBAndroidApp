package org.jorgetargz.movies.framework.list_trending_movies

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
import org.jorgetargz.movies.domain.use_cases.ListTrendingMoviesUseCase
import org.jorgetargz.movies.framework.utils.Utils
import org.jorgetargz.movies.utils.NetworkResult
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ListTrendingMoviesViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val loadTrendingMoviesUseCase: ListTrendingMoviesUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<ListTrendingMoviesContract.ListTrendingMoviesState> by lazy {
        MutableStateFlow(ListTrendingMoviesContract.ListTrendingMoviesState())
    }
    val uiState: StateFlow<ListTrendingMoviesContract.ListTrendingMoviesState> = _uiState

    private fun loadTrendingMovies() {
        viewModelScope.launch {
            if (Utils.hasInternetConnection(context = appContext)) {
                loadTrendingMoviesUseCase.invoke()
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
                                    movies = result.data ?: emptyList(),
                                    moviesFiltered = result.data ?: emptyList(),
                                    isLoading = false
                                )
                            }
                        }
                    }
            } else {
                _uiState.update {
                    it.copy(
                        error = "no hay internet cargando de cache.",
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun filterMovies(nombre: String) {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        moviesFiltered = _uiState.value.movies.filter { movie ->
                            movie.title.contains(nombre, true)
                        }
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun handleEvent(event: ListTrendingMoviesContract.ListTrendingMoviesEvent) {
        when (event) {
            is ListTrendingMoviesContract.ListTrendingMoviesEvent.LoadTrendingMoviesMovies -> loadTrendingMovies()
            is ListTrendingMoviesContract.ListTrendingMoviesEvent.FilterTrendingMoviesMovies -> filterMovies(event.nombre)
        }
    }
}