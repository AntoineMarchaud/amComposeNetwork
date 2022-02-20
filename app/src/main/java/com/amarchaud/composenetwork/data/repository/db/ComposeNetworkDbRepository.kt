package com.amarchaud.composenetwork.data.repository.db

import com.amarchaud.composenetwork.data.db.models.MeEntityModel
import com.amarchaud.composenetwork.domain.models.MeModel
import kotlinx.coroutines.flow.Flow

interface ComposeNetworkDbRepository {
    fun getMasterFolder(): Flow<MeEntityModel?>
    suspend fun insertUser(login: String, password: String)
    suspend fun updateUser(myselfModel: MeModel)
    suspend fun deleteUser()
}