package org.jorgetargz.movies.framework.details_tv_show

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
import org.jorgetargz.movies.domain.models.TVShow
import org.jorgetargz.movies.domain.use_cases.tv_shows.LoadCachedTVShowByIdUseCase
import org.jorgetargz.movies.domain.use_cases.tv_shows.LoadTVShowByIdUseCase
import org.jorgetargz.movies.framework.utils.Utils
import org.jorgetargz.movies.utils.NetworkResult
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailsTVShowViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val loadTVShowByIdUseCase: LoadTVShowByIdUseCase,
    private val loadCachedTVShowByIdUseCase: LoadCachedTVShowByIdUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<DetailsTVShowContract.DetailsTVShowState> by lazy {
        MutableStateFlow(DetailsTVShowContract.DetailsTVShowState())
    }
    val uiState: StateFlow<DetailsTVShowContract.DetailsTVShowState> = _uiState

    private fun loadTVShowById(id: Int) {
        viewModelScope.launch {
            if (Utils.hasInternetConnection(context = appContext)) {
                loadTVShowByIdUseCase.invoke(id)
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
                                    tvShow = result.data ?: TVShow(),
                                    isLoading = false
                                )
                            }
                        }
                    }
            } else {
                loadCachedTVShowByIdUseCase.invoke(id)
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
                                    tvShow = result.data ?: TVShow(),
                                    isLoading = false
                                )
                            }
                        }
                    }
            }
        }
    }


    fun handleEvent(event: DetailsTVShowContract.DetailsTVShowEvent) {
        when (event) {
            is DetailsTVShowContract.DetailsTVShowEvent.LoadTVShow -> loadTVShowById(event.id)
        }
    }
}