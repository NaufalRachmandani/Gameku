package com.naufal.gameku.domain.game.use_case

import androidx.paging.PagingData
import com.naufal.gameku.data.game.remote.model.GamesResponse
import com.naufal.gameku.domain.GameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGamesUseCase @Inject constructor(private val gameRepository: GameRepository) {
    operator fun invoke(search: String): Flow<PagingData<GamesResponse.Result>> = gameRepository.getGames(search)
}