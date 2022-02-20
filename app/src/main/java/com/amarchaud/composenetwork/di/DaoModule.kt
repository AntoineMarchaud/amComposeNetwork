package com.amarchaud.composenetwork.di

import android.content.Context
import androidx.room.Room
import com.amarchaud.composenetwork.data.db.AppDb
import com.amarchaud.composenetwork.data.db.ComposeNetworkDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): AppDb {
        return Room.databaseBuilder(appContext, AppDb::class.java, "composenetwork_db")
            .build()
    }

    @Singleton
    @Provides
    fun provideComposeNetworkDao(appDb: AppDb): ComposeNetworkDao {
        return appDb.getComposeNetworkDao()
    }
}