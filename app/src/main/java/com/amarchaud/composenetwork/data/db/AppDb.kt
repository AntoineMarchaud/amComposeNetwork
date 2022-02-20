package com.amarchaud.composenetwork.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amarchaud.composenetwork.data.adapters.DateTimeAdapter
import com.amarchaud.composenetwork.data.db.models.MeEntityModel

@Database(
    entities = [
        MeEntityModel::class
    ], version = 1, exportSchema = false
)
@TypeConverters(
    DateTimeAdapter.LocalDateDbConverter::class
)
abstract class AppDb : RoomDatabase() {
    abstract fun getComposeNetworkDao() : ComposeNetworkDao
}

