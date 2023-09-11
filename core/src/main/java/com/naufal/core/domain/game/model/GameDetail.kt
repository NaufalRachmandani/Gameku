package com.naufal.core.domain.game.model

data class GameDetail(
    val id: Int? = null,
    val name: String? = null,
    val description: String? = null,
    val released: String? = null,
    val backgroundImage: String? = null,
    val developers: List<Developer?>? = null,
    val genres: List<Genre?>? = null,
) {
    data class Developer(
        val id: Int? = null,
        val name: String? = null,
        val slug: String? = null,
        val gamesCount: Int? = null,
        val imageBackground: String? = null
    )

    data class Genre(
        val name: String? = null,
    )
}