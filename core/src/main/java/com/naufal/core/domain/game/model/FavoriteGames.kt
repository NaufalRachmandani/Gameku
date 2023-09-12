package com.naufal.core.domain.game.model

data class FavoriteGames(
    var id: Int? = null,
    val name: String? = null,
    val released: String? = null,
    val backgroundImage: String? = null,
    val genres: String? = null,
)