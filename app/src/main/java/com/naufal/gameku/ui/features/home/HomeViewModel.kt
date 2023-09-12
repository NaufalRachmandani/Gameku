package com.naufal.gameku.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.naufal.core.domain.game.model.Games
import com.naufal.core.domain.game.use_case.GetGamesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getGamesUseCase: GetGamesUseCase
) : ViewModel() {

    private val _gamesState: MutableStateFlow<PagingData<Games.Result>> =
        MutableStateFlow(value = PagingData.empty())
    val gamesState: MutableStateFlow<PagingData<Games.Result>> get() = _gamesState

    private var searchJob: Job? = null

    init {
        getGames()
    }

    fun getGames(search: String = "") {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            getGamesUseCase(search = search)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    _gamesState.value = it
                }
        }
    }
}