package com.naufal.gameku.data.game

import com.naufal.gameku.data.common.AppResult
import com.naufal.gameku.data.common.errorBody
import com.naufal.gameku.data.game.model.entity.GameEntity
import com.naufal.gameku.data.game.model.response.GameDetailResponse
import com.naufal.gameku.data.game.model.response.GamesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GameRepository @Inject constructor(
    private val gameService: GameService,
    private val gameDatabase: GameDatabase,
) {
    suspend fun getGames(search: String = "", page: Int = 1): AppResult<Flow<GamesResponse?>> {
        val response = gameService.getGames(search = search, page = page)

        if (response.isSuccessful) {
            val result = response.body()
            return AppResult.OnSuccess(
                flow {
                    emit(result)
                }.flowOn(Dispatchers.IO)
            )
        } else {
            val errorBody =
                errorBody<GamesResponse>(
                    response.body()?.toString()
                )
            return AppResult.OnFailure(
                flow { emit(errorBody) }.flowOn(Dispatchers.IO),
                response.code(),
            )
        }
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

    suspend fun getFavoriteGames(): AppResult<Flow<List<GameEntity>?>> {
        return try {
            AppResult.OnSuccess(
                flow { emit(gameDatabase.gameDao().getGames()) }.flowOn(Dispatchers.IO)
            )
        } catch (e: Exception) {
            AppResult.OnFailure(
                message = e.message,
            )
        }
    }

    suspend fun saveFavoriteGame(gameEntity: GameEntity): AppResult<Flow<Boolean>> {
        return try {
            gameDatabase.gameDao().saveGame(gameEntity)
            AppResult.OnSuccess(
                flow { emit(true) }.flowOn(Dispatchers.IO)
            )
        } catch (e: Exception) {
            AppResult.OnFailure(
                message = e.message,
            )
        }
    }

    suspend fun deleteFavoriteGame(gameId: Int): AppResult<Flow<Boolean>> {
        return try {
            gameDatabase.gameDao().deleteGame(gameId)
            AppResult.OnSuccess(
                flow { emit(true) }.flowOn(Dispatchers.IO)
            )
        } catch (e: Exception) {
            AppResult.OnFailure(
                message = e.message,
            )
        }
    }
}