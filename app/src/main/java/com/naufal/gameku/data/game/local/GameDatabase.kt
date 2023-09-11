package com.naufal.gameku.data.game.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.naufal.gameku.data.game.local.GameDao
import com.naufal.gameku.data.game.local.model.GameEntity

@Database(
    entities = [GameEntity::class],
    version = 1
)
abstract class GameDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

}