package com.naufal.gameku.data.game.remote.model

import com.google.gson.annotations.SerializedName

data class GameDetailResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("released")
    val released: String? = null,
    @SerializedName("background_image")
    val backgroundImage: String? = null,
    @SerializedName("developers")
    val developers: List<Developer?>? = null,
    @SerializedName("genres")
    val genres: List<Genre?>? = null,
) {
    data class Developer(
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("slug")
        val slug: String? = null,
        @SerializedName("games_count")
        val gamesCount: Int? = null,
        @SerializedName("image_background")
        val imageBackground: String? = null
    )

    data class Genre(
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("slug")
        val slug: String? = null,
        @SerializedName("games_count")
        val gamesCount: Int? = null,
        @SerializedName("image_background")
        val imageBackground: String? = null
    )
}