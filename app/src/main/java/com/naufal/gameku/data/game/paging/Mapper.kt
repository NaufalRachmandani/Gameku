package com.naufal.gameku.data.game.paging

import com.naufal.gameku.data.game.model.response.GamesResponse

fun List<GamesResponse.Result?>?.toGamesList(): List<GamesResponse.Result> {
    val gamesList = mutableListOf<GamesResponse.Result>()

    this?.forEach {
        it?.let { it1 -> gamesList.add(it1) }
    }

    return gamesList.toList()
}