package org.jorgetargz.movies.framework.details_person

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
import org.jorgetargz.movies.domain.models.Person
import org.jorgetargz.movies.domain.use_cases.persons.LoadCachedPersonByIdUseCase
import org.jorgetargz.movies.domain.use_cases.persons.LoadPersonByIdUseCase
import org.jorgetargz.movies.framework.utils.Utils
import org.jorgetargz.movies.utils.NetworkResult
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailsPersonViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val loadPersonByIdUseCase: LoadPersonByIdUseCase,
    private val loadCachedPersonByIdUseCase: LoadCachedPersonByIdUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<DetailsPersonContract.DetailsPersonState> by lazy {
        MutableStateFlow(DetailsPersonContract.DetailsPersonState())
    }
    val uiState: StateFlow<DetailsPersonContract.DetailsPersonState> = _uiState

    private fun loadMovieById(id: Int) {
        viewModelScope.launch {
            if (Utils.hasInternetConnection(context = appContext)) {
                loadPersonByIdUseCase.invoke(id)
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
                                    person = result.data ?: Person(),
                                    isLoading = false
                                )
                            }
                        }
                    }
            } else {
                loadCachedPersonByIdUseCase.invoke(id)
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
                                    person = result.data ?: Person(),
                                    isLoading = false
                                )
                            }
                        }
                    }
            }
        }
    }


    fun handleEvent(event: DetailsPersonContract.DetailsPersonEvent) {
        when (event) {
            is DetailsPersonContract.DetailsPersonEvent.LoadPerson -> loadMovieById(event.id)
        }
    }
}