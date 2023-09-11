package com.naufal.gameku.ui.features.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.core.data.common.addOnResultListener
import com.naufal.core.data.game.GameRepositoryImpl
import com.naufal.core.data.game.local.model.GameEntity
import com.naufal.core.data.game.remote.model.GameDetailResponse
import com.naufal.core.domain.game.use_case.GetGameDetailUseCase
import com.naufal.gameku.ui.util.gameDetailResponseToStringGenres
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameDetailViewModel @Inject constructor(
    private val gameRepositoryImpl: GameRepositoryImpl,
    private val gameDetailUseCase: GetGameDetailUseCase
) : ViewModel() {

    private val _gameDetailState = MutableStateFlow(GameDetailState())
    val gameDetailState = _gameDetailState.asStateFlow()

    private val _favoriteGameState = MutableStateFlow(FavoriteGameState())
    val favoriteGameState = _favoriteGameState.asStateFlow()

    private val _saveFavoriteGameState = MutableStateFlow(SaveFavoriteGameState())
    val saveFavoriteGameState = _saveFavoriteGameState.asStateFlow()

    fun getGameDetail(gameId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _gameDetailState.emit(GameDetailState(loading = true))

            gameDetailUseCase(gameId).addOnResultListener(
                onSuccess = {
                    it?.collectLatest { data ->
                        _gameDetailState.emit(GameDetailState(gameDetail = data))
                    }
                },
                onFailure = { data, code, message ->
                    _gameDetailState.emit(GameDetailState(error = true, message = message))
                },
                onError = {
                    _gameDetailState.emit(GameDetailState(error = true, message = it?.message))
                }
            )
        }
    }

    fun checkFavoriteGame(gameId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _favoriteGameState.emit(FavoriteGameState(loading = true))

            gameRepositoryImpl.checkFavoriteGame(gameId).addOnResultListener(
                onSuccess = {
                    it?.collectLatest { data ->
                        _favoriteGameState.emit(FavoriteGameState(isFavorite = data))
                    }
                },
                onFailure = { data, code, message ->
                    _favoriteGameState.emit(FavoriteGameState(error = true))
                },
                onError = {
                    _favoriteGameState.emit(FavoriteGameState(error = true))
                }
            )
        }
    }

    fun saveFavoriteGame(gameDetail: GameDetailResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            _saveFavoriteGameState.emit(SaveFavoriteGameState(loading = true))

            gameRepositoryImpl.saveFavoriteGame(
                GameEntity(
                    id = gameDetail.id,
                    name = gameDetail.name,
                    released = gameDetail.released,
                    backgroundImage = gameDetail.backgroundImage,
                    genres = gameDetail.genres.gameDetailResponseToStringGenres(),
                )
            ).addOnResultListener(
                onSuccess = {
                    it?.collectLatest { data ->
                        _saveFavoriteGameState.emit(SaveFavoriteGameState(success = data))
                        _favoriteGameState.emit(FavoriteGameState(isFavorite = data))
                    }
                },
                onFailure = { data, code, message ->
                    _saveFavoriteGameState.emit(SaveFavoriteGameState(error = true))
                },
                onError = {
                    _saveFavoriteGameState.emit(SaveFavoriteGameState(error = true))
                }
            )
        }
    }

    fun unFavoriteGame(gameId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _saveFavoriteGameState.emit(SaveFavoriteGameState(loading = true))

            gameRepositoryImpl.deleteFavoriteGame(gameId).addOnResultListener(
                onSuccess = {
                    it?.collectLatest { data ->
                        _saveFavoriteGameState.emit(SaveFavoriteGameState(successUnFav = data))
                        _favoriteGameState.emit(FavoriteGameState(isFavorite = !data))
                    }
                },
                onFailure = { data, code, message ->
                    _saveFavoriteGameState.emit(SaveFavoriteGameState(errorUnFav = true))
                },
                onError = {
                    _saveFavoriteGameState.emit(SaveFavoriteGameState(errorUnFav = true))
                }
            )
        }
    }

    data class GameDetailState(
        val loading: Boolean? = null,
        val error: Boolean? = null,
        val message: String? = null,
        val gameDetail: GameDetailResponse? = null,
    )

    data class FavoriteGameState(
        val loading: Boolean? = null,
        val error: Boolean? = null,
        val isFavorite: Boolean? = null,
    )

    data class SaveFavoriteGameState(
        val loading: Boolean? = null,
        val error: Boolean? = null,
        val errorUnFav: Boolean? = null,
        val success: Boolean? = null,
        val successUnFav: Boolean? = null,
    )
}