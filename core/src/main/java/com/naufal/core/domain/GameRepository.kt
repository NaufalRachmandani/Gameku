package com.naufal.core.domain

import androidx.paging.PagingData
import com.naufal.core.data.common.AppResult
import com.naufal.core.data.game.local.model.GameEntity
import com.naufal.core.data.game.remote.model.GamesResponse
import com.naufal.core.domain.game.model.GameDetail
import com.naufal.core.domain.game.model.Games
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getGames(search: String = ""): Flow<PagingData<Games.Result>>

    suspend fun getGameDetail(gameId: Int): AppResult<Flow<GameDetail?>>

    suspend fun getFavoriteGames(): AppResult<Flow<List<GameEntity>?>>

    suspend fun checkFavoriteGame(gameId: Int): AppResult<Flow<Boolean>>

    suspend fun saveFavoriteGame(gameEntity: GameEntity): AppResult<Flow<Boolean>>

    suspend fun deleteFavoriteGame(gameId: Int): AppResult<Flow<Boolean>>
}