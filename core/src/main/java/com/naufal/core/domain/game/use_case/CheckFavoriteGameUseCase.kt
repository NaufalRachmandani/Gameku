package com.naufal.core.domain.game.use_case

import com.naufal.core.data.common.AppResult
import com.naufal.core.domain.GameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckFavoriteGameUseCase @Inject constructor(private val gameRepository: GameRepository) {
    suspend operator fun invoke(gameId: Int): AppResult<Flow<Boolean>> =
        gameRepository.checkFavoriteGame(gameId)
}