package org.jorgetargz.movies.framework.details_movie

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
import org.jorgetargz.movies.domain.models.Movie
import org.jorgetargz.movies.domain.use_cases.movies.LoadCachedMovieByIdUseCase
import org.jorgetargz.movies.domain.use_cases.movies.LoadMovieByIdUseCase
import org.jorgetargz.movies.framework.utils.Utils
import org.jorgetargz.movies.utils.NetworkResult
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailsMovieViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val loadMovieByIdUseCase: LoadMovieByIdUseCase,
    private val loadCachedMovieByIdUseCase: LoadCachedMovieByIdUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<DetailsMovieContract.DetailsMovieState> by lazy {
        MutableStateFlow(DetailsMovieContract.DetailsMovieState())
    }
    val uiState: StateFlow<DetailsMovieContract.DetailsMovieState> = _uiState

    private fun loadMovieById(id: Int) {
        viewModelScope.launch {
            if (Utils.hasInternetConnection(context = appContext)) {
                loadMovieByIdUseCase.invoke(id)
                    .catch(action = { cause ->
                        _uiState.update {
                            it.copy(
                                error = cause.message,
                                isLoading = false
                            )
                        }
                        Timber.e(cause)
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
                                Timber.e(result.message)
                            }
                            is NetworkResult.Loading -> _uiState.update {
                                it.copy(isLoading = true)
                            }
                            is NetworkResult.Success -> _uiState.update {
                                it.copy(
                                    movie = result.data ?: Movie(),
                                    isLoading = false
                                )
                            }
                        }
                    }
            } else {
                loadCachedMovieByIdUseCase.invoke(id)
                    .catch(action = { cause ->
                        _uiState.update {
                            it.copy(
                                error = cause.message,
                                isLoading = false
                            )
                        }
                        Timber.e(cause)
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
                                Timber.e(result.message)
                            }
                            is NetworkResult.Loading -> _uiState.update {
                                it.copy(isLoading = true)
                            }
                            is NetworkResult.Success -> _uiState.update {
                                it.copy(
                                    error = "Loaded from cache",
                                    movie = result.data ?: Movie(),
                                    isLoading = false
                                )
                            }
                        }
                    }
            }
        }
    }


    fun handleEvent(event: DetailsMovieContract.DetailsMovieEvent) {
        when (event) {
            is DetailsMovieContract.DetailsMovieEvent.LoadMovie -> loadMovieById(event.id)
        }
    }
}