package com.naufal.gameku.data.game.remote

import com.naufal.gameku.data.game.remote.model.GameDetailResponse
import com.naufal.gameku.data.game.remote.model.GamesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GameService {
    @GET("games")
    suspend fun getGames(
        @Query("key") key: String = "",
        @Query("page_size") pageSize: Int = 10,
        @Query("page") page: Int = 1,
        @Query("search") search: String = "",
    ): GamesResponse

    @GET("games/{gameId}")
    suspend fun getGameDetail(
        @Path("gameId") gameId: Int?,
        @Query("key") key: String = "",
    ): Response<GameDetailResponse>
}