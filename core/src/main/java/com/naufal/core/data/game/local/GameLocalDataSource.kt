package com.naufal.core.data.game.local

import com.naufal.core.data.common.AppResult
import com.naufal.core.data.game.local.model.GameEntity
import com.naufal.core.domain.game.mapper.toFavoriteGames
import com.naufal.core.domain.game.model.FavoriteGames
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GameLocalDataSource @Inject constructor(
    private val gameDatabase: GameDatabase,
) {
    suspend fun getFavoriteGames(): AppResult<Flow<List<FavoriteGames>>> {
        return try {
            AppResult.OnSuccess(
                flow { emit(gameDatabase.gameDao().getGames().toFavoriteGames()) }.flowOn(
                    Dispatchers.IO
                )
            )
        } catch (e: Exception) {
            AppResult.OnFailure(
                message = e.message,
            )
        }
    }

    suspend fun checkFavoriteGame(gameId: Int): AppResult<Flow<Boolean>> {
        return try {
            val result = gameDatabase.gameDao().checkFavoriteGame(gameId)
            AppResult.OnSuccess(
                flow { emit(result) }.flowOn(Dispatchers.IO)
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