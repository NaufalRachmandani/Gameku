package com.naufal.gameku.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.naufal.gameku.data.common.addOnResultListener
import com.naufal.gameku.data.game.GameRepository
import com.naufal.gameku.data.game.model.response.GamesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _gamesState: MutableStateFlow<PagingData<GamesResponse.Result>> = MutableStateFlow(value = PagingData.empty())
    val gamesState: MutableStateFlow<PagingData<GamesResponse.Result>> get() = _gamesState

    private var searchJob: Job? = null

    init {
        getGames()
    }

    fun getGames(search: String = "") {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            gameRepository.getGames(search = search)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    _gamesState.value = it
                }
        }
    }
}