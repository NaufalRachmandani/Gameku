package com.naufal.core.domain.game.use_case

import com.naufal.core.data.common.AppResult
import com.naufal.core.domain.GameRepository
import com.naufal.core.domain.game.model.GameDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGameDetailUseCase @Inject constructor(private val gameRepository: GameRepository) {
    suspend operator fun invoke(gameId: Int): AppResult<Flow<GameDetail?>> =
        gameRepository.getGameDetail(gameId)
}