package com.naufal.gameku.domain.game.use_case

import com.naufal.gameku.data.common.AppResult
import com.naufal.gameku.data.game.remote.model.GameDetailResponse
import com.naufal.gameku.domain.GameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGameDetailUseCase @Inject constructor(private val gameRepository: GameRepository) {
    suspend operator fun invoke(gameId: Int): AppResult<Flow<GameDetailResponse?>> = gameRepository.getGameDetail(gameId)
}