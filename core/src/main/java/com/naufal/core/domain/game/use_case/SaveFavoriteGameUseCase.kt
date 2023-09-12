package com.naufal.core.domain.game.use_case

import com.naufal.core.data.common.AppResult
import com.naufal.core.data.game.local.model.GameEntity
import com.naufal.core.domain.GameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveFavoriteGameUseCase @Inject constructor(private val gameRepository: GameRepository) {
    suspend operator fun invoke(gameEntity: GameEntity): AppResult<Flow<Boolean>> =
        gameRepository.saveFavoriteGame(gameEntity)
}