package com.amarchaud.composenetwork.di

import android.content.Context
import com.amarchaud.composenetwork.BuildConfig
import com.amarchaud.composenetwork.data.adapters.DateTimeAdapter
import com.amarchaud.composenetwork.data.api.ComposeNetworkApi
import com.amarchaud.composenetwork.data.db.ComposeNetworkDao
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(DateTimeAdapter.LocalDateTimeAdapter)
            .build()

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideHeaderInterceptor(@ApplicationContext context : Context, composeNetworkDao: ComposeNetworkDao): HeaderInterceptor {
        return HeaderInterceptor(context, composeNetworkDao)
    }

    @Singleton
    @Provides
    fun provideClient(
        headerInterceptor: HeaderInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(httpLoggingInterceptor)
            }
            addInterceptor(headerInterceptor)
            connectTimeout(5, TimeUnit.SECONDS)
            writeTimeout(5, TimeUnit.SECONDS)
            readTimeout(5, TimeUnit.SECONDS)
        }
        return builder.build()
    }


    @Singleton
    @Provides
    fun provideDomainApi(
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): ComposeNetworkApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.ENDPOINT)
            .addConverterFactory(
                MoshiConverterFactory.create(moshi)
            )
            .client(okHttpClient)
            .build()
            .create(ComposeNetworkApi::class.java)
    }
}