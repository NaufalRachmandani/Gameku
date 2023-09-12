package com.naufal.core.domain.game.use_case

import androidx.paging.PagingData
import com.naufal.core.data.game.remote.model.GamesResponse
import com.naufal.core.domain.GameRepository
import com.naufal.core.domain.game.model.Games
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGamesUseCase @Inject constructor(private val gameRepository: GameRepository) {
    operator fun invoke(search: String): Flow<PagingData<Games.Result>> = gameRepository.getGames(search)
}