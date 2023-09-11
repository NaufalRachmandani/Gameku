package com.naufal.gameku.domain

import androidx.paging.PagingData
import com.naufal.gameku.data.common.AppResult
import com.naufal.gameku.data.game.local.model.GameEntity
import com.naufal.gameku.data.game.remote.model.GameDetailResponse
import com.naufal.gameku.data.game.remote.model.GamesResponse
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getGames(search: String = ""): Flow<PagingData<GamesResponse.Result>>

    suspend fun getGameDetail(gameId: Int): AppResult<Flow<GameDetailResponse?>>

    suspend fun getFavoriteGames(): AppResult<Flow<List<GameEntity>?>>

    suspend fun checkFavoriteGame(gameId: Int): AppResult<Flow<Boolean>>

    suspend fun saveFavoriteGame(gameEntity: GameEntity): AppResult<Flow<Boolean>>

    suspend fun deleteFavoriteGame(gameId: Int): AppResult<Flow<Boolean>>
}