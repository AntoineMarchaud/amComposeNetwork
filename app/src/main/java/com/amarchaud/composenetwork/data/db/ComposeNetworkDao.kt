package com.amarchaud.composenetwork.data.db

import androidx.room.*
import com.amarchaud.composenetwork.data.db.models.MeEntityModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ComposeNetworkDao {

    @Insert
    suspend fun insertUser(entityUser: MeEntityModel)

    @Update
    suspend fun updateUser(entityUser: MeEntityModel)

    @Query("SELECT * from me")
    suspend fun getUser(): MeEntityModel?

    @Query("SELECT * from me")
    fun getUserFlow(): Flow<MeEntityModel?>

    @Query("SELECT COUNT(*) from me")
    fun isUserExist(): Flow<Int?>

    @Query("DELETE FROM me")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteUser(entityUser: MeEntityModel)
}