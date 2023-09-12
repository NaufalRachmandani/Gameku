package com.naufal.gameku.ui.util

import com.naufal.core.data.game.remote.model.GameDetailResponse
import com.naufal.core.data.game.remote.model.GamesResponse
import com.naufal.core.domain.game.model.GameDetail
import com.naufal.core.domain.game.model.Games

fun List<Games.Result.Genre?>?.gamesToStringGenres(): String {
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

fun List<GameDetail.Genre?>?.gameDetailToStringGenres(): String {
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

fun List<GameDetail.Developer?>?.toStringDevelopers(): String {
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