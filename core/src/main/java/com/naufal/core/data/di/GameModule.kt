package com.naufal.core.data.di

import android.content.Context
import androidx.room.Room
import com.naufal.core.data.game.local.GameDatabase
import com.naufal.core.data.game.GameRepositoryImpl
import com.naufal.core.data.game.local.GameLocalDataSource
import com.naufal.core.data.game.remote.GameRemoteDataSource
import com.naufal.core.data.game.remote.GameService
import com.naufal.core.domain.GameRepository
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
    fun provideGameLocalDataSource(
        gameDatabase: GameDatabase,
    ): GameLocalDataSource {
        return GameLocalDataSource(gameDatabase)
    }

    @Provides
    fun provideGameRemoteDataSource(
        gameService: GameService,
    ): GameRemoteDataSource {
        return GameRemoteDataSource(gameService)
    }

    @Provides
    fun provideGameRepository(
        gameRemoteDataSource: GameRemoteDataSource,
        gameLocalDataSource: GameLocalDataSource,
    ): GameRepository {
        return GameRepositoryImpl(gameRemoteDataSource, gameLocalDataSource)
    }
}