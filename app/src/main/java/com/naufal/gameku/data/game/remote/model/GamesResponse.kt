package com.naufal.gameku.data.game.remote.model


import com.google.gson.annotations.SerializedName

data class GamesResponse(
    @SerializedName("count")
    val count: Int? = null,
    @SerializedName("next")
    val next: String? = null,
    @SerializedName("previous")
    val previous: String? = null,
    @SerializedName("results")
    val results: List<Result?>? = null,
) {
    data class Result(
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("released")
        val released: String? = null,
        @SerializedName("background_image")
        val backgroundImage: String? = null,
        @SerializedName("genres")
        val genres: List<Genre?>? = null,
    ) {

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
}