package org.jorgetargz.movies.framework.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.jorgetargz.movies.framework.main.MainContract.State
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState: MutableStateFlow<State> by lazy {
        MutableStateFlow(State())
    }
    val uiState: StateFlow<State> = _uiState


    fun handleEvent(event: MainContract.Event) {
        when (event) {
            MainContract.Event.MensajeMostrado -> {
                _uiState.update { it.copy(error = null) }
            }
        }


    }




}