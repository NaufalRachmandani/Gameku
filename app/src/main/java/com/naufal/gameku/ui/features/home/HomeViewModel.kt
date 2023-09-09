package com.naufal.gameku.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.gameku.data.common.addOnResultListener
import com.naufal.gameku.data.game.GameRepository
import com.naufal.gameku.data.game.model.response.GamesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    init {
        getGames()
    }

    private fun getGames() {
        viewModelScope.launch(Dispatchers.IO) {
            _homeState.emit(HomeState(loading = true))

            gameRepository.getGames().addOnResultListener(
                onSuccess = {
                    it?.collectLatest { data ->
                        _homeState.emit(
                            HomeState(
                                loading = false,
                                games = data?.results
                            )
                        )
                    }
                },
                onFailure = { data, code, message ->
                    _homeState.emit(HomeState(loading = false, error = true, message = message))
                },
                onError = {
                    _homeState.emit(HomeState(loading = false, error = true, message = it?.message))
                }
            )
        }
    }

    data class HomeState(
        val loading: Boolean? = null,
        val error: Boolean? = null,
        val message: String? = null,
        val games: List<GamesResponse.Result?>? = null,
    )
}