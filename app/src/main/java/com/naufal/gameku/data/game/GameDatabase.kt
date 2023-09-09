package com.naufal.gameku.data.game

import androidx.room.Database
import androidx.room.RoomDatabase
import com.naufal.gameku.data.game.model.entity.GameEntity

@Database(
    entities = [GameEntity::class],
    version = 1
)
abstract class GameDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

}