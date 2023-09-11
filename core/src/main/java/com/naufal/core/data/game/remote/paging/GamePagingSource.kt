package com.naufal.core.data.game.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.naufal.core.data.game.remote.GameService
import com.naufal.core.data.game.remote.model.GamesResponse
import retrofit2.HttpException
import java.io.IOException

class GamePagingSource(
    private val search: String,
    private val gameService: GameService
) : PagingSource<Int, GamesResponse.Result>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GamesResponse.Result> {
        return try {
            val currentPage = params.key ?: 1
            val games = gameService.getGames(
                search = search,
                page = currentPage
            )
            LoadResult.Page(
                data = games.results.toGamesList(),
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (games.results?.isEmpty() == true) null else currentPage + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GamesResponse.Result>): Int? {
        return state.anchorPosition
    }
}