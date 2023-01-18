package org.jorgetargz.movies.framework.list_trending_persons

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
import org.jorgetargz.movies.domain.use_cases.persons.LoadCachedTrendingPersonsUseCase
import org.jorgetargz.movies.domain.use_cases.persons.LoadTrendingPersonsUseCase
import org.jorgetargz.movies.framework.utils.Utils
import org.jorgetargz.movies.utils.NetworkResult
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ListTrendingPersonsViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val loadTrendingPersonsUseCase: LoadTrendingPersonsUseCase,
    private val loadCachedTrendingPersonsUseCase: LoadCachedTrendingPersonsUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<ListTrendingPersonsContract.ListTrendingPersonsState> by lazy {
        MutableStateFlow(ListTrendingPersonsContract.ListTrendingPersonsState())
    }
    val uiState: StateFlow<ListTrendingPersonsContract.ListTrendingPersonsState> = _uiState

    private fun loadTrendingMovies() {
        viewModelScope.launch {
            if (Utils.hasInternetConnection(context = appContext)) {
                loadTrendingPersonsUseCase.invoke()
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
                                    persons = result.data ?: emptyList(),
                                    personsFiltered = result.data ?: emptyList(),
                                    isLoading = false
                                )
                            }
                        }
                    }
            } else {
                loadCachedTrendingPersonsUseCase.invoke()
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
                                    persons = result.data ?: emptyList(),
                                    personsFiltered = result.data ?: emptyList(),
                                    isLoading = false
                                )
                            }
                        }
                    }
            }
        }
    }

    private fun filterMovies(nombre: String) {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        personsFiltered = _uiState.value.persons.filter { person ->
                            person.name.contains(nombre, true)
                        }
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun handleEvent(event: ListTrendingPersonsContract.ListTrendingPersonsEvent) {
        when (event) {
            is ListTrendingPersonsContract.ListTrendingPersonsEvent.LoadTrendingPersons -> loadTrendingMovies()
            is ListTrendingPersonsContract.ListTrendingPersonsEvent.FilterTrendingPersons -> filterMovies(
                event.nombre
            )
        }
    }
}