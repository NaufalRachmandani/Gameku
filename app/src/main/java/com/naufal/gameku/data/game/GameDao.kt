package com.naufal.gameku.data.game

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.naufal.gameku.data.game.model.entity.GameEntity

@Dao
interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGame(gameEntity: GameEntity)

    @Query("SELECT * FROM GameEntity")
    suspend fun getGames(): List<GameEntity>

    @Query("SELECT EXISTS(SELECT * FROM GameEntity WHERE id = :gameId)")
    suspend fun checkFavoriteGame(gameId: Int): Boolean

    @Query("DELETE FROM GameEntity WHERE id = :gameId")
    suspend fun deleteGame(gameId: Int)
}