package com.naufal.gameku.data.di

import android.content.Context
import androidx.room.Room
import com.naufal.gameku.data.game.GameDatabase
import com.naufal.gameku.data.game.GameRepository
import com.naufal.gameku.data.game.GameService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object GameModule {
    @Provides
    fun provideGameService(@Named("game-api") retrofit: Retrofit): GameService {
        return retrofit.create(GameService::class.java)
    }

    @Provides
    fun provideGameDatabase(@ApplicationContext context: Context): GameDatabase {
        return try {
            Room.databaseBuilder(context, GameDatabase::class.java, "game_database")
                .build()
        } catch (e: NullPointerException) {
            throw Exception("Data module initialization at Main Application is required.")
        }
    }

    @Provides
    fun provideGameRepository(
        gameService: GameService,
        gameDatabase: GameDatabase
    ): GameRepository {
        return GameRepository(gameService, gameDatabase)
    }
}