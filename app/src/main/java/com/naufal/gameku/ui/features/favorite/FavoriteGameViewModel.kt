package com.naufal.gameku.ui.features.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.core.data.common.addOnResultListener
import com.naufal.core.data.game.GameRepositoryImpl
import com.naufal.core.data.game.local.model.GameEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteGameViewModel @Inject constructor(
    private val gameRepositoryImpl: GameRepositoryImpl
) : ViewModel() {

    private val _favoriteGameState = MutableStateFlow(FavoriteGameState())
    val favoriteGameState = _favoriteGameState.asStateFlow()

    fun getGames() {
        viewModelScope.launch(Dispatchers.IO) {
            _favoriteGameState.emit(FavoriteGameState(loading = true))

            gameRepositoryImpl.getFavoriteGames().addOnResultListener(
                onSuccess = {
                    it?.collectLatest { data ->
                        _favoriteGameState.emit(FavoriteGameState(games = data))
                    }
                },
                onFailure = { data, code, message ->
                    _favoriteGameState.emit(FavoriteGameState(error = true, message = message))
                },
                onError = {
                    _favoriteGameState.emit(FavoriteGameState(error = true, message = it?.message))
                }
            )
        }
    }

    data class FavoriteGameState(
        val loading: Boolean? = null,
        val error: Boolean? = null,
        val message: String? = null,
        val games: List<GameEntity>? = null,
    )
}