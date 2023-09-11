package com.naufal.gameku.data.game.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.naufal.gameku.data.common.AppResult
import com.naufal.gameku.data.common.errorBody
import com.naufal.gameku.data.game.remote.model.GameDetailResponse
import com.naufal.gameku.data.game.remote.model.GamesResponse
import com.naufal.gameku.data.game.remote.paging.GamePagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GameRemoteDataSource @Inject constructor(
    private val gameService: GameService
) {
    fun getGames(search: String = ""): Flow<PagingData<GamesResponse.Result>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GamePagingSource(search, gameService)
            }
        ).flow
    }

    suspend fun getGameDetail(gameId: Int): AppResult<Flow<GameDetailResponse?>> {
        val response = gameService.getGameDetail(gameId = gameId)

        if (response.isSuccessful) {
            val result = response.body()
            return AppResult.OnSuccess(
                flow {
                    emit(result)
                }.flowOn(Dispatchers.IO)
            )
        } else {
            val errorBody =
                errorBody<GameDetailResponse>(
                    response.body()?.toString()
                )
            return AppResult.OnFailure(
                flow { emit(errorBody) }.flowOn(Dispatchers.IO),
                response.code(),
            )
        }
    }
}