package com.naufal.core.data.game.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.naufal.core.data.common.AppResult
import com.naufal.core.data.game.remote.model.GamesResponse
import com.naufal.core.data.game.remote.paging.GamePagingSource
import com.naufal.core.domain.game.mapper.toGameDetail
import com.naufal.core.domain.game.model.GameDetail
import com.naufal.core.domain.game.model.Games
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GameRemoteDataSource @Inject constructor(
    private val gameService: GameService
) {
    fun getGames(search: String = ""): Flow<PagingData<Games.Result>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GamePagingSource(search, gameService)
            }
        ).flow
    }

    suspend fun getGameDetail(gameId: Int): AppResult<Flow<GameDetail?>> {
        val response = gameService.getGameDetail(gameId = gameId)

        if (response.isSuccessful) {
            val result = response.body()
            return AppResult.OnSuccess(
                flow {
                    emit(result?.toGameDetail())
                }.flowOn(Dispatchers.IO)
            )
        } else {
            return AppResult.OnFailure(
                code = response.code(),
                message = response.message()
            )
        }
    }
}