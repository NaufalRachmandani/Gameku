package com.naufal.gameku.data.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Collections
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private const val BASE_URL = "https://api.rawg.io/api/"

    @Provides
    @Named("interceptor-api")
    fun provideOkHttpClientApi(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Named("game-api")
    fun provideRetrofitApi(@Named("interceptor-api") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().serializeNulls().create()
                )
            )
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }
}