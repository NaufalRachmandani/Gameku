package com.naufal.gameku.ui.util

import com.naufal.core.data.game.remote.model.GameDetailResponse
import com.naufal.core.data.game.remote.model.GamesResponse

fun List<GamesResponse.Result.Genre?>?.gamesResponseToStringGenres(): String {
    val stringBuilder = StringBuilder()
    if (this.isNullOrEmpty()) {
        stringBuilder.append("-")
    } else {
        this.forEach {
            if (stringBuilder.isNotEmpty())
                stringBuilder.append(", ")

            stringBuilder.append(it?.name)
        }
    }

    return stringBuilder.toString()
}

fun List<GameDetailResponse.Genre?>?.gameDetailResponseToStringGenres(): String {
    val stringBuilder = StringBuilder()
    if (this.isNullOrEmpty()) {
        stringBuilder.append("-")
    } else {
        this.forEach {
            if (stringBuilder.isNotEmpty())
                stringBuilder.append(", ")

            stringBuilder.append(it?.name)
        }
    }

    return stringBuilder.toString()
}

fun List<GameDetailResponse.Developer?>?.toStringDevelopers(): String {
    val stringBuilder = StringBuilder()
    if (this.isNullOrEmpty()) {
        stringBuilder.append("-")
    } else {
        this.forEach {
            if (stringBuilder.isNotEmpty())
                stringBuilder.append(", ")

            stringBuilder.append(it?.name)
        }
    }

    return stringBuilder.toString()
}