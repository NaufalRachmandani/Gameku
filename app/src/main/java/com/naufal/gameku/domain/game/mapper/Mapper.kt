package com.naufal.gameku.domain.game.mapper

import com.naufal.gameku.data.game.remote.model.GameDetailResponse
import com.naufal.gameku.domain.game.model.GameDetail

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