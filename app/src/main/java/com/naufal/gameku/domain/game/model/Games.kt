package com.naufal.gameku.domain.game.model

data class Games(
    val count: Int? = null,
    val next: String? = null,
    val previous: String? = null,
    val results: List<Result?>? = null,
) {
    data class Result(
        val id: Int? = null,
        val name: String? = null,
        val released: String? = null,
        val backgroundImage: String? = null,
        val genres: List<Genre?>? = null,
    ) {

        data class Genre(
            val id: Int? = null,
            val name: String? = null,
            val slug: String? = null,
            val gamesCount: Int? = null,
            val imageBackground: String? = null
        )
    }
}