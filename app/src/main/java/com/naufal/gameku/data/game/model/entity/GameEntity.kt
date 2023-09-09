package com.naufal.gameku.data.game.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class GameEntity(

    @PrimaryKey
    var id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("released")
    val released: String? = null,
    @SerializedName("background_image")
    val backgroundImage: String? = null,
    @SerializedName("genres")
    val genres: String? = null,
)