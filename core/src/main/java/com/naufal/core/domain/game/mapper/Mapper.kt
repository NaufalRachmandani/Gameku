package com.naufal.core.domain.game.mapper

import com.naufal.core.data.game.remote.model.GameDetailResponse
import com.naufal.core.data.game.remote.model.GamesResponse
import com.naufal.core.domain.game.model.GameDetail
import com.naufal.core.domain.game.model.Games

fun List<GamesResponse.Result?>?.toGamesList(): List<Games.Result> {
    val gamesList = mutableListOf<Games.Result>()

    this?.forEach {
        it?.let { response ->
            gamesList.add(
                Games.Result(
                    id = response.id,
                    name = response.name,
                    released = response.released,
                    backgroundImage = response.backgroundImage,
                    genres = response.genres.toGamesGenre(),
                )
            )
        }
    }

    return gamesList.toList()
}

fun List<GamesResponse.Result.Genre?>?.toGamesGenre(): List<Games.Result.Genre> {
    val list = mutableListOf<Games.Result.Genre>()
    this?.forEach {
        list.add(
            Games.Result.Genre(
                name = it?.name,
            )
        )
    }

    return list.toList()
}

fun List<GameDetailResponse.Genre?>?.toGameDetailGenre(): List<GameDetail.Genre> {
    val list = mutableListOf<GameDetail.Genre>()
    this?.forEach {
        list.add(
            GameDetail.Genre(
                name = it?.name,
            )
        )
    }

    return list.toList()
}

fun GameDetailResponse.toGameDetail(): GameDetail {
    return GameDetail(
        id = id,
        name = name,
        description = description,
        released = released,
        backgroundImage = backgroundImage,
        genres = genres.toGameDetailGenre()
    )
}