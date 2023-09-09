package com.naufal.gameku.ui.util

import com.naufal.gameku.data.game.model.response.GamesResponse

fun List<GamesResponse.Result.Genre?>?.toStringGenres(): String {
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