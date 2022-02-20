package com.amarchaud.composenetwork.data.repository.db

import com.amarchaud.composenetwork.data.adapters.DateTimeAdapter
import com.amarchaud.composenetwork.data.db.ComposeNetworkDao
import com.amarchaud.composenetwork.data.db.models.MeEntityModel
import com.amarchaud.composenetwork.domain.models.MeModel
import javax.inject.Inject

class ComposeNetworkDbRepositoryImp @Inject constructor(
    private val dao: ComposeNetworkDao,
) : ComposeNetworkDbRepository {
    override fun getMasterFolder() = dao.getUserFlow()

    override suspend fun insertUser(login: String, password: String) {
        dao.insertUser(
            entityUser = MeEntityModel(
                login = login,
                password = password
            )
        )
    }

    override suspend fun updateUser(myselfModel: MeModel) {
        dao.getUser()?.let {
            dao.updateUser(it.apply {
                this.firstName = myselfModel.firstName
                this.lastName = myselfModel.lastName
                this.id = myselfModel.rootItem.id
                this.parentId = myselfModel.rootItem.parentId
                this.isDir = myselfModel.rootItem.isDir
                this.name = myselfModel.rootItem.name
                this.modificationDate =
                    DateTimeAdapter.LocalDateDbConverter.localDateToString(myselfModel.rootItem.modificationDate)
                this.size = myselfModel.rootItem.size
                this.contentType = myselfModel.rootItem.contentType
            })
        }
    }

    override suspend fun deleteUser() {
        dao.deleteAll()
    }
}