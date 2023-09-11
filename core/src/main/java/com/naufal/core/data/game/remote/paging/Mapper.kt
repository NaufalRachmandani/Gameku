package com.naufal.core.data.game.remote.paging

import com.naufal.core.data.game.remote.model.GamesResponse

fun List<GamesResponse.Result?>?.toGamesList(): List<GamesResponse.Result> {
    val gamesList = mutableListOf<GamesResponse.Result>()

    this?.forEach {
        it?.let { it1 -> gamesList.add(it1) }
    }

    return gamesList.toList()
}