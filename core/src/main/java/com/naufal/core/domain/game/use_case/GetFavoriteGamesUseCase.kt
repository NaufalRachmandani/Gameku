package com.naufal.core.domain.game.use_case

import com.naufal.core.data.common.AppResult
import com.naufal.core.domain.GameRepository
import com.naufal.core.domain.game.model.FavoriteGames
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteGamesUseCase @Inject constructor(private val gameRepository: GameRepository) {
    suspend operator fun invoke(): AppResult<Flow<List<FavoriteGames>>> =
        gameRepository.getFavoriteGames()
}