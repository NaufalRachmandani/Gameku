package com.naufal.gameku.data.game

import com.naufal.gameku.data.game.model.response.GameDetailResponse
import com.naufal.gameku.data.game.model.response.GamesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GameService {
    @GET("games")
    suspend fun getGames(
        @Query("key") key: String = "api key",
        @Query("page_size") pageSize: Int = 10,
        @Query("page") page: Int = 1,
        @Query("search") search: String = "",
    ): Response<GamesResponse>

    @GET("games/{gameId}")
    suspend fun getGameDetail(
        @Query("key") key: String = "api key",
        @Path("gameId") gameId: Int?,
    ): Response<GameDetailResponse>
}