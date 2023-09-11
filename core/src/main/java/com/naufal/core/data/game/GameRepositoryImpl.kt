package com.naufal.core.data.game

import androidx.paging.PagingData
import com.naufal.core.data.common.AppResult
import com.naufal.core.data.game.local.GameLocalDataSource
import com.naufal.core.data.game.local.model.GameEntity
import com.naufal.core.data.game.remote.GameRemoteDataSource
import com.naufal.core.data.game.remote.model.GameDetailResponse
import com.naufal.core.data.game.remote.model.GamesResponse
import com.naufal.core.domain.GameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val gameRemoteDataSource: GameRemoteDataSource,
    private val gameLocalDataSource: GameLocalDataSource,
) : GameRepository {
    override fun getGames(search: String): Flow<PagingData<GamesResponse.Result>> =
        gameRemoteDataSource.getGames(search)

    override suspend fun getGameDetail(gameId: Int): AppResult<Flow<GameDetailResponse?>> =
        gameRemoteDataSource.getGameDetail(gameId)

    override suspend fun getFavoriteGames(): AppResult<Flow<List<GameEntity>?>> =
        gameLocalDataSource.getFavoriteGames()

    override suspend fun checkFavoriteGame(gameId: Int): AppResult<Flow<Boolean>> =
        gameLocalDataSource.checkFavoriteGame(gameId)

    override suspend fun saveFavoriteGame(gameEntity: GameEntity): AppResult<Flow<Boolean>> =
        gameLocalDataSource.saveFavoriteGame(gameEntity)

    override suspend fun deleteFavoriteGame(gameId: Int): AppResult<Flow<Boolean>> =
        gameLocalDataSource.deleteFavoriteGame(gameId)
}